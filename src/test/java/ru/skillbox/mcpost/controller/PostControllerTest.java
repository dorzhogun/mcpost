package ru.skillbox.mcpost.controller;

import net.javacrumbs.jsonunit.JsonAssert;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.mcpost.StringTestUtils;
import ru.skillbox.mcpost.AbstractTest;
import ru.skillbox.mcpost.dto.*;
import ru.skillbox.mcpost.model.enums.LikeType;
import ru.skillbox.mcpost.model.enums.PostType;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
public class PostControllerTest extends AbstractTest {
    private static final String TEST_TIME = "2024-06-04T12:50:36Z";
    private static final String ENTRY_URL = "/api/v1/post";
    private static final String PRINCIPAL_ID = "7099e6e4-f0b9-4aa6-a263-05e7eeae1604";
    private static final String BEARER = "Bearer " + PRINCIPAL_ID;

    @Test
    public void whenGetPostByIdThenReturnNotFound() throws Exception {
        mockMvc.perform(get(ENTRY_URL + "/120")
                        .header(HttpHeaders.AUTHORIZATION, BEARER))
                .andExpect(status().isNotFound());
    }

    @Test
    public void whenGetPostsThenReturnForbidden() throws Exception {
        mockMvc.perform(get(ENTRY_URL))
                .andExpect(status().isForbidden());
    }

    @Test
    public void whenGetPostByIdThenReturnPostById() throws Exception {
        String actualResult = mockMvc.perform(get(ENTRY_URL + "/1")
                        .header(HttpHeaders.AUTHORIZATION, BEARER))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        String expectedResult = StringTestUtils
                .readStringFromResource("response/find_post_by_id.json");
        JsonAssert.assertJsonEquals(expectedResult, actualResult);
    }

    @Test
    public void whenGetPostsThenReturnPostList() throws Exception {
        String actualResult = mockMvc.perform(get(ENTRY_URL)
                        .header(HttpHeaders.AUTHORIZATION, BEARER))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        String expectedResult = StringTestUtils
                .readStringFromResource("response/find_all_posts_response.json");
        JsonAssert.assertJsonEquals(expectedResult, actualResult);
    }

    @Test
    public void whenCreatePostThenReturnPostDto() throws Exception {
        String actualResult =  mockMvc
                .perform(post(ENTRY_URL)
                        .header(HttpHeaders.AUTHORIZATION, BEARER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createPostRq())))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        String expectedResult = StringTestUtils
                .readStringFromResource("response/create_post_response.json");
        JsonAssert.assertJsonEquals(actualResult, expectedResult);
    }

    @Test
    public void whenUpdatePostThenReturnPostDto() throws Exception {
        PostDto postDto = new PostDto(1L, UUID.fromString(PRINCIPAL_ID), "update_title",
                "update_post_text", Instant.parse(TEST_TIME), "update_image_path",
                new ArrayList<>(), Instant.parse(TEST_TIME), Instant.parse(TEST_TIME),
                PostType.POSTED, false, false, 0,0);
        String actualResult = mockMvc
                .perform(put(ENTRY_URL)
                        .header(HttpHeaders.AUTHORIZATION, BEARER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        String expectedResult = StringTestUtils
                .readStringFromResource("response/update_post_response.json");
        JsonAssert.assertJsonEquals(expectedResult, actualResult);
    }

    @Test
    public void whenDeletePostThenReturnStatusOk() throws Exception {
        mockMvc.perform(delete(ENTRY_URL + "/1").header(HttpHeaders.AUTHORIZATION, BEARER))
                .andExpect(status().isOk());
    }

    @Test
    public void whenCreatePostLikeThenReturnLikeDto() throws Exception {
        LikeDto likeDto = new LikeDto(LikeType.POST, "HEART");
        String actualResult = mockMvc
                .perform(post(ENTRY_URL + "/1/like")
                        .header(HttpHeaders.AUTHORIZATION, BEARER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(likeDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        String expectedResult = StringTestUtils
                .readStringFromResource("response/create_post_like_response.json");
        JsonAssert.assertJsonEquals(actualResult, expectedResult);
    }

    @Test
    public void whenDeletePostLikeThenReturnPostDto() throws Exception {
        String actualResult = mockMvc.perform(delete(ENTRY_URL + "/2/like")
                        .header(HttpHeaders.AUTHORIZATION, BEARER))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        String expectedResult = StringTestUtils.readStringFromResource("response/delete_post_like_response.json");
        JsonAssert.assertJsonEquals(actualResult, expectedResult);
    }

    @Test
    public void whenCreateCommentThenReturnCommentDto() throws Exception {
        String getResult = mockMvc.perform(post(ENTRY_URL + "/1/comment")
                        .header(HttpHeaders.AUTHORIZATION, BEARER)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCommentRq())))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        CommentDto commentDto = objectMapper.readValue(getResult, CommentDto.class);
        commentDto.setTime(Instant.parse("2024-06-04T12:57:36Z"));
        commentDto.setTimeChanged(Instant.parse("2024-06-04T12:57:36Z"));
        String actualResult = objectMapper.writeValueAsString(commentDto);
        String expectedResult = StringTestUtils
                .readStringFromResource("response/create_post_comment_response.json");
        JsonAssert.assertJsonEquals(actualResult, expectedResult);
    }

    @Test
    public void whenGetPostCommentsThenReturnCommentsRs() throws Exception {
        String actualResult = mockMvc.perform(get(ENTRY_URL + "/1/comment")
                        .header(HttpHeaders.AUTHORIZATION, BEARER))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        String expectedResult = StringTestUtils
                .readStringFromResource("response/post_comments_response.json");
        JsonAssert.assertJsonEquals(actualResult, expectedResult);
    }

    @Test
    public void whenGetCommentCommentsThenReturnCommentsRs() throws Exception {
        String actualResult = mockMvc
                .perform(get(ENTRY_URL + "/1/comment/1/subcomment")
                        .header(HttpHeaders.AUTHORIZATION, BEARER))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        String expectedResult = StringTestUtils
                .readStringFromResource("/response/comment_comments_response.json");
        JsonAssert.assertJsonEquals(actualResult, expectedResult);
    }

    @Test
    public void whenSetPostCommentLikeThenReturnCommentDto() throws Exception {
        String actualResult = mockMvc
                .perform(post(ENTRY_URL +"/1/comment/1/like")
                        .header(HttpHeaders.AUTHORIZATION, BEARER))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        String expectedResult = StringTestUtils
                .readStringFromResource("/response/set_post_comment_like_response.json");
        JsonAssert.assertJsonEquals(actualResult, expectedResult);
    }

    @Test
    public void whenRemoveCommentLikeThenReturnCommentDto() throws Exception {
        String actualResult = mockMvc
                .perform(delete( ENTRY_URL + "/1/comment/1")
                        .header(HttpHeaders.AUTHORIZATION, BEARER))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString(StandardCharsets.UTF_8);
        String expectedResult = StringTestUtils
                .readStringFromResource("/response/remove_comment_like_response.json");
        JsonAssert.assertJsonEquals(actualResult, expectedResult);
    }

    @Test
    public void whenRemoveCommentThenReturnStatusOk() throws Exception {
        mockMvc.perform(delete( ENTRY_URL + "/1/comment/1")
                        .header(HttpHeaders.AUTHORIZATION, BEARER))
                .andExpect(status().isOk());
    }

    private CommentRq createCommentRq() {
        CommentRq commentRq = new CommentRq();
        commentRq.setCommentText("Comment_to_post");
        return commentRq;
    }

    private PostRq createPostRq() {
        PostRq postRq = new PostRq();
        postRq.setTitle("new_title1");
        postRq.setPostText("new_post_text1");
        postRq.setPublishDate(Instant.parse(TEST_TIME));
        postRq.setImagePath("new_image_path1");
        postRq.setTime(Instant.parse(TEST_TIME));
        postRq.setTimeChanged(Instant.parse(TEST_TIME));
        return postRq;
    }
}

