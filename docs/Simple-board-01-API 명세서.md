<br>

# 전체 API 목록

| **분류** | **Method** | **API 명** | **설명** |
| --- | --- | --- | --- |
| 게시글 조회 API | GET | /articles | 게시글 전체 조회 |
|| GET | /articles/{id} | 게시글 단건 조회 |
| 게시글 등록 API | POST | /article | 게시글 등록 |
| 게시글 수정 API | PUT | /articles/{id} | 게시글 수정 |
| 게시글 삭제 API | DELETE | /articles | 게시글 여러건 삭제 |
|| DELETE | /articles/{id} | 게시글 단건 삭제 |

<br>

# 공통 Response 필드

Response Body에는 아래의 필드가 포함됩니다.

| **필드** | **타입** | **필수 여부** | **설명** |
| --- | --- | --- | --- |
| status | Boolean | `필수` | api 호출 결과 상태<br><br>* true : 올바른 요청<br>* false : 잘못된 요청 |
| message | String | `필수` | 결과 메시지 |
| result | Json Object / Json Array | `선택` | 응답 데이터<br><br>* 1건 조회 : Json Object<br>* 전체 조회 : Json Array |

<br>

# 게시글 조회 API

## 단건 조회

### Request

#### Request Syntax

```bash
curl -X GET 'http://localhost/articles/{id}'
```

<br>


#### API 호출 방식

| **메서드** | **요청 url** |
| --- | --- |
| GET | `/articles/{id}` |

* path parameter : article id

<br>


### Response

#### 게시글 조회 성공 응답 예시

```java
{
    "status": true,
    "message": "article 1 조회",
    "result": {
        "title": "게시글 1번입니다.",
        "content": "안녕하세요 게시글 1번 본문입니다.",
	"modified_at": "2023-12-10T21:54:32.321",
        "created_at": "2023-12-10T21:54:32.321"
   }
}
```

<br>


#### 실패 응답 예시

```java
{
    "status": false,
    "message": "잘못된 게시글 번호입니다."
}
```

<br>


## 전체 조회

### Request

#### Request Syntax

```bash
curl -X GET 'http://localhost/articles'
```

<br>


#### API 호출 방식

| **메서드** | **요청 url** |
| --- | --- |
| GET | `/articles` |

<br>


### Response

#### 게시글 조회 성공 응답 예시

```java
{
    "status": true,
    "message": "article 전체 조회",
    "result": [
        {
            "title": "게시글 1번입니다.",
            "content": "안녕하세요 게시글 1번 본문입니다.",
            "modified_at": "2023-12-10T21:54:32.321",
            "created_at": "2023-12-10T21:54:32.321"
      },
      {
            "title": "2번입니다.",
            "content": "안녕하세요 게시글 2번 본문입니다.",
            "modified_at": "2023-12-10T21:54:32.321",
            "created_at": "2023-12-10T21:54:32.321"
      },
      {
            "title": "게시글 3번.",
            "content": "안녕하세요 게시글 3번 본문입니다.",
            "modified_at": "2023-12-10T21:54:32.321",
            "created_at": "2023-12-10T21:54:32.321"
      }
}
```

<br>


# 게시글 등록 API

### Request

#### Request Syntax

```bash
curl -X POST 'http://localhost/article'
```

<br>


#### API 호출 방식

| **메서드** | **요청 url** |
| --- | --- |
| POST | `/article` |

<br>


#### Request Body

```java
{
    "title": "",
    "content": ""
}
```

| 파라미터 | 타입  | 필수 여부 | 설명  |
| --- | --- | --- | --- |
| title | String | `필수` | 제목  |
| content | String | `필수` | 본문  |

<br>


### Response

#### Response Header

```bash
HTTP/1.1 201 Created
Location: http://localhost:8080/articles/{id}
```

<br>


#### Response Body

#### 게시글 등록 성공 응답 예시

```java
{
    "status": true,
    "message": "등록 되었습니다."
}
```

#### 실패 응답 예시 - 제목이 없는 경우

```java
{
    "status": false,
    "message": "제목을 입력해주세요."
}
```

* 제목, 본문에 값이 없는 경우 실패 응답

<br>


# 게시글 수정 API

### Request

#### Request Syntax

```bash
curl -X PUT 'http://localhost/articles/{id}'
```

<br>


#### API 호출 방식

| **메서드** | **요청 url** |
| --- | --- |
| PUT | `/articles/{id}` |

* Path Parameter : article id

<br>


#### Request Body

```java
{
    "id": 1,
    "title": "",
    "content": ""
}
```

| 파라미터 | 타입  | 필수 여부 | 설명  |
| --- | --- | --- | --- |
| id  | Long | `필수` | 수정할 article id |
| title | String | `필수` | 제목  |
| content | String | `필수` | 본문  |

<br>


### Response

#### Response Header

```bash
HTTP/1.1 201 Created
Location: http://localhost:8080/articles/{id}
```

<br>


#### Response Body

#### 게시글 등록 성공 응답 예시

```java
{
    "status": true,
    "message": "수정 되었습니다."
}
```

#### 실패 응답 예시 - 제목이 없는 경우

```java
{
    "status": false,
    "message": "제목을 입력해주세요."
}
```

* 제목, 본문에 값이 없는 경우 실패 응답

#### 실패 응답 예시 - 없는 게시글 id에 대한 수정 요청

```java
{
    "status": false,
    "message": "잘못된 요청입니다."
}
```

<br>


# 게시글 삭제 API

## 게시글 1개 삭제

### Request

#### Request Syntax

```bash
curl -X DELETE 'http://localhost/articles/{id}'
```

#### API 호출 방식

| **메서드** | **요청 url** |
| --- | --- |
| DELETE | `/articles/{id}` |

* Path Parameter : article id

<br>


### Response

#### Response Header

```bash
HTTP/1.1 204 No Content
Location: http://localhost:8080/articles
```

<br>


## 게시글 여러개 삭제

### Request

#### Request Syntax

```bash
curl -X DELETE 'http://localhost/articles'
```

<br>


#### API 호출 방식

| **메서드** | **요청 url** |
| --- | --- |
| DELETE | `/articles` |

<br>


#### Request Body

```bash
{
  "id": [1,2,3]
}
```

| 파라미터 | 타입  | 필수 여부 | 설명  |
| --- | --- | --- | --- |
| id  | List<Long> | `필수` | 삭제할 article id 번호 리스트 |

<br>


### Response

#### Response Header

```bash
HTTP/1.1 204 No Content
Location: http://localhost:8080/articles
```