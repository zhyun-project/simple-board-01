package kim.zhyun.board.controller;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kim.zhyun.board.data.ArticleDto;
import kim.zhyun.board.exception.ArticleNotFoundException;
import kim.zhyun.board.service.ArticleService;
import kim.zhyun.board.type.ExceptionType;
import net.minidev.json.JSONArray;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static java.time.LocalDateTime.now;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArticleController.class)
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
    
    @DisplayName("전체 게시글 조회 - 게시글 없음")
    @Test
    void findAll_size_zero() throws Exception {
        // When & Then
        mvc.perform(get("/articles").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status").value(true))
                .andExpect(jsonPath("message").value("article 전체 조회"))
                .andDo(print());
    }
    
    @DisplayName("전체 게시글 조회 - 게시글 있음")
    @Test
    void findAll() throws Exception {
        List<ArticleDto> dtos = List.of(
                ArticleDto.of(1L, "title 1", "안뇽하십니꽈 1", now().plusHours(1), now().plusHours(1)),
                ArticleDto.of(2L, "title 2", "안뇽하십니꽈 2", now().plusHours(2), now().plusHours(2)),
                ArticleDto.of(3L, "title 3", "안뇽하십니꽈 3", now().plusHours(3), now().plusHours(3))
        );
        when(articleService.findAll()).thenReturn(dtos);
        
        // When & Then
        mvc.perform(get("/articles").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("article 전체 조회"))
                .andExpect(jsonPath("$.result").value(getJsonArray(dtos)))
                .andDo(print());
    }
    
    @DisplayName("게시글 1개 조회")
    @Test
    void findById() throws Exception {
        // given
        long articleId = 1L;
        ArticleDto articleDto = ArticleDto.of(articleId, "title", "content", now(), now());
        when(articleService.findById(articleId)).thenReturn(articleDto);
        
        // When & Then
        mvc.perform(get("/articles/" + articleId).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(true))
                .andExpect(jsonPath("$.message").value("article " + articleId + " 조회"))
                .andExpect(jsonPath("$.result").value(getJsonObject(articleDto)))
                .andDo(print());
    }
    
    @DisplayName("게시글 1개 조회 - 없는 게시글 조회")
    @Test
    void findById_non_existent() throws Exception {
        // given
        long articleId = 1L;
        given(articleService.findById(articleId)).willThrow(new ArticleNotFoundException(ExceptionType.ARTICLE_NOT_FOUND));
        
        // When & Then
        mvc.perform(get("/articles/" + articleId).contentType(MediaType.APPLICATION_JSON))
                .andExpect((result) -> assertTrue("", result.getResolvedException() instanceof ArticleNotFoundException))
                .andExpect(status().is4xxClientError())
                .andExpect(jsonPath("status").value(false))
                .andExpect(jsonPath("message").value(ExceptionType.ARTICLE_NOT_FOUND.getDescription()))
                .andDo(print());
    }
    
    
    
    
    private Object getJsonObject(ArticleDto articleDto) throws ParseException, JsonProcessingException {
        return parser.parse(mapper.writeValueAsString(articleDto));
    }
    
    private Object getJsonArray(List<ArticleDto> dtos) {
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
    
}