package kim.zhyun.board.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kim.zhyun.board.data.ArticleCreateRequest;
import kim.zhyun.board.data.ArticleDto;
import kim.zhyun.board.data.ArticleUpdateRequest;
import kim.zhyun.board.data.ValidExceptionResponse;
import kim.zhyun.board.exception.ArticleNotFoundException;
import kim.zhyun.board.service.ArticleService;
import net.minidev.json.JSONArray;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static java.time.LocalDateTime.now;
import static kim.zhyun.board.type.ExceptionType.ARTICLE_NOT_FOUND;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class ArticleControllerTest {
    
    @MockBean
    private ArticleService articleService;
    
    private final MockMvc mvc;
    private final ObjectMapper mapper;
    private final JSONParser parser;
    public ArticleControllerTest(@Autowired MockMvc mvc,
                                 @Autowired ObjectMapper mapper) {
        this.mvc = mvc;
        this.mapper = mapper.registerModule(new JavaTimeModule());
        this.parser = new JSONParser(JSONParser.MODE_PERMISSIVE);
    }
    
    @DisplayName("게시글 조회 Case 모음")
    @Nested
    class SelectArticles {
        @DisplayName("전체 조회 - 게시글 없음")
        @Test
        void findAll_size_zero() throws Exception {
            // When & Then
            mvc.perform(get("/articles").contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("status").value(true))
                    .andExpect(jsonPath("message").value("article 전체 조회"))
                    .andDo(print());
        }
        
        @DisplayName("전체 조회")
        @Test
        void findAll() throws Exception {
            List<ArticleDto> dtos = List.of(
                    ArticleDto.of(1L, "title 1", "안뇽하십니꽈 1", now().plusHours(1), now().plusHours(1)),
                    ArticleDto.of(2L, "title 2", "안뇽하십니꽈 2", now().plusHours(2), now().plusHours(2)),
                    ArticleDto.of(3L, "title 3", "안뇽하십니꽈 3", now().plusHours(3), now().plusHours(3))
            );
            
            // When
            when(articleService.findAll()).thenReturn(dtos);
            
            // Then
            mvc.perform(get("/articles").contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(true))
                    .andExpect(jsonPath("$.message").value("article 전체 조회"))
                    .andExpect(jsonPath("$.result").value(getJsonArrayDto(dtos)))
                    .andDo(print());
            
            verify(articleService).findAll();
        }
        
        @DisplayName("1건 조회")
        @Test
        void findById() throws Exception {
            // given
            long articleId = 1L;
            ArticleDto articleDto = ArticleDto.of(articleId, "title", "content", now(), now());
            
            // When
            when(articleService.findById(articleId)).thenReturn(articleDto);
            
            // Then
            mvc.perform(get("/articles/{id}", articleId).contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.status").value(true))
                    .andExpect(jsonPath("$.message").value("article " + articleId + " 조회"))
                    .andExpect(jsonPath("$.result").value(getJsonObject(articleDto)))
                    .andDo(print());
            
            verify(articleService).findById(articleId);
        }
        
        @DisplayName("1건 조회 - 없는 게시글 조회")
        @Test
        void findById_non_existent() throws Exception {
            // given
            long articleId = 1L;
            
            // When
            given(articleService.findById(articleId)).willThrow(new ArticleNotFoundException(ARTICLE_NOT_FOUND));
            
            // Then
            mvc.perform(get("/articles/{id}", articleId).contentType(MediaType.APPLICATION_JSON))
                    .andExpect((result) -> assertTrue("", result.getResolvedException() instanceof ArticleNotFoundException))
                    .andExpect(status().is4xxClientError())
                    .andExpect(jsonPath("status").value(false))
                    .andExpect(jsonPath("message").value(ARTICLE_NOT_FOUND.getDescription()))
                    .andDo(print());
            
            verify(articleService).findById(articleId);
        }
    }
    
    @DisplayName("게시글 등록 Case 모음")
    @Nested
    class SaveArticle {
        
        @DisplayName("등록 - 성공")
        @Test
        void save() throws Exception {
            // given
            ArticleCreateRequest request = ArticleCreateRequest.of("제목 1", "졸리다 😳");
            long saveId = 10L;
            
            // when
            when(articleService.save(request)).thenReturn(saveId);
            
            // then
            mvc.perform(post("/article")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.status").value(true))
                    .andExpect(jsonPath("$.message").value("등록되었습니다."))
                    .andExpect(redirectedUrl("http://localhost/articles/" + saveId))
                    .andDo(print());
            
            verify(articleService).save(request);
        }
        
        @DisplayName("등록 실패 Case 모음")
        @Nested
        class SaveFailTest {
            
            @DisplayName("사유 : 제목 없음")
            @Test
            void save_failed_because_title_is_empty() throws Exception {
                // given
                ArticleCreateRequest request = ArticleCreateRequest.of("", "졸리다 😳");
                long saveId = 10L;
                
                List<ValidExceptionResponse> exceptionResponse = List.of(
                        ValidExceptionResponse.builder()
                                .field("title")
                                .message("제목을 입력해주세요").build());
                // run
                run(request, exceptionResponse);
            }
            
            @DisplayName("사유 : 내용 없음")
            @Test
            void save_failed_because_content_is_empty() throws Exception {
                // given
                ArticleCreateRequest request = ArticleCreateRequest.of("타이틀", "");
                long saveId = 10L;
                
                List<ValidExceptionResponse> exceptionResponse = List.of(
                        ValidExceptionResponse.builder()
                                .field("content")
                                .message("내용을 입력해주세요").build());
                
                // run
                run(request, exceptionResponse);
            }
            
            @Disabled("reponse body의 result 리스트에서 객체 출력 순서가 랜덤하기 때문에, 테스트 실행시 response body 값 확인 필요")
            @DisplayName("사유 : 제목, 내용 없음")
            @Test
            void save_failed_because_all_field_is_empty() throws Exception {
                // given
                ArticleCreateRequest request = ArticleCreateRequest.of("", "");
                long saveId = 10L;
                
                List<ValidExceptionResponse> exceptionResponse = List.of(
                        ValidExceptionResponse.builder()
                                .field("title")
                                .message("제목을 입력해주세요").build(),
                        ValidExceptionResponse.builder()
                                .field("content")
                                .message("내용을 입력해주세요").build());
                
                // run
                run(request, exceptionResponse);
            }
            
            public void run(ArticleCreateRequest request, List<ValidExceptionResponse> exceptionResponse) throws Exception {
                // when
                when(articleService.save(request)).thenThrow(new ArticleNotFoundException(ARTICLE_NOT_FOUND));
                
                // then
                mvc.perform(post("/article")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(request)))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.status").value(false))
                        .andExpect(jsonPath("$.message").value("valid error"))
                        .andExpect(jsonPath("$.result").value(getJsonArrayValidException(exceptionResponse)))
                        .andDo(print());
                
                verify(articleService, times(0)).save(request);
            }
        }
    }
    
    
    @DisplayName("게시글 수정 Case 모음")
    @Nested
    class UpdateArticle {
        
        @DisplayName("수정 - 성공")
        @Test
        void update() throws Exception {
            // given
            long updateId = 10L;
            ArticleUpdateRequest request = ArticleUpdateRequest.of(updateId, "제목 1 수정", "우리 집에 홍주가 업데이트 되었다 😋");
            
            // when
            willDoNothing().given(articleService).update(request);
            
            // then
            mvc.perform(put("/articles/{id}", updateId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(mapper.writeValueAsString(request)))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.status").value(true))
                    .andExpect(jsonPath("$.message").value("수정되었습니다."))
                    .andExpect(redirectedUrl("http://localhost/articles/" + updateId))
                    .andDo(print());
            
            verify(articleService).update(request);
        }
        
        @DisplayName("수정 실패 Case 모음")
        @Nested
        class UpdateFailTest {
            
            @DisplayName("사유 : 제목 없음")
            @Test
            void update_failed_because_title_is_empty() throws Exception {
                // given
                long updateId = 10L;
                ArticleUpdateRequest request = ArticleUpdateRequest.of(updateId, "", "우리 집에 홍주가 업데이트 되었다 😋");
                
                // when
                willDoNothing().given(articleService).update(request);
                
                List<ValidExceptionResponse> exceptionResponse = List.of(
                        ValidExceptionResponse.builder()
                                .field("title")
                                .message("제목을 입력해주세요").build());
                // run
                run(updateId, request, exceptionResponse);
            }
            
            @DisplayName("사유 : 내용 없음")
            @Test
            void update_failed_because_content_is_empty() throws Exception {
                // given
                long updateId = 10L;
                ArticleUpdateRequest request = ArticleUpdateRequest.of(updateId, "제목 1 수정", "");
                
                // when
                willDoNothing().given(articleService).update(request);
                
                List<ValidExceptionResponse> exceptionResponse = List.of(
                        ValidExceptionResponse.builder()
                                .field("content")
                                .message("내용을 입력해주세요").build());
                
                // run
                run(updateId, request, exceptionResponse);
            }
            
            @Disabled("reponse body의 result 리스트에서 객체 출력 순서가 랜덤하기 때문에, 테스트 실행시 response body 값 확인 필요")
            @DisplayName("사유 : 제목, 내용 없음")
            @Test
            void update_failed_because_all_field_is_empty() throws Exception {
                // given
                long updateId = 10L;
                ArticleUpdateRequest request = ArticleUpdateRequest.of(updateId, "", "");
                
                List<ValidExceptionResponse> exceptionResponse = List.of(
                        ValidExceptionResponse.builder()
                                .field("title")
                                .message("제목을 입력해주세요").build(),
                        ValidExceptionResponse.builder()
                                .field("content")
                                .message("내용을 입력해주세요").build());
                
                // run
                run(updateId, request, exceptionResponse);
            }
            
            public void run(long id, ArticleUpdateRequest request, List<ValidExceptionResponse> exceptionResponse) throws Exception {
                // when
                willThrow(new ArticleNotFoundException(ARTICLE_NOT_FOUND)).given(articleService).update(request);
                
                // then
                mvc.perform(put("/articles/{id}", id)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(request)))
                        .andExpect(status().isBadRequest())
                        .andExpect(jsonPath("$.status").value(false))
                        .andExpect(jsonPath("$.message").value("valid error"))
                        .andExpect(jsonPath("$.result").value(getJsonArrayValidException(exceptionResponse)))
                        .andDo(print());
                
                verify(articleService, times(0)).update(request);
            }
        }
    }
    
    
    
    
    
    private Object getJsonObject(ArticleDto articleDto) throws ParseException, JsonProcessingException {
        return parser.parse(mapper.writeValueAsString(articleDto));
    }
    
    private Object getJsonArrayDto(List<ArticleDto> dtos) {
        JSONArray array = new JSONArray();
        dtos.forEach(dto -> {
            try {
                array.add(parser.parse(mapper.writeValueAsString(dto)));
            } catch (ParseException | JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        return array;
    }
    
    private Object getJsonArrayValidException(List<ValidExceptionResponse> objs) {
        JSONArray array = new JSONArray();
        objs.forEach(obj -> {
            try {
                array.add(parser.parse(mapper.writeValueAsString(obj)));
            } catch (ParseException | JsonProcessingException e) {
                e.printStackTrace();
            }
        });
        return array;
    }
    
}