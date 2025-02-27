//package com.example.recipebook.Post.presenter;
//
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import com.example.recipebook.Post.api.PostApiService;
//import com.example.recipebook.Post.contract.PostContract;
//import com.example.recipebook.Post.model.PostModel;
//
//import junit.framework.TestCase;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Captor;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class PostPresenterTest{
//
//    @Mock
//    private PostContract.View view;
//
//    @Mock
//    private PostApiService apiService;
//
//    @Mock
//    private Call<List<PostModel>> postCall;
//
//    @Mock
//    private Call<PostModel> createPostCall;
//
//    @Mock
//    private Call<Void> deletePostCall;
//
//    @Captor
//    private ArgumentCaptor<Callback<List<PostModel>>> postCallbackCaptor;
//
//    @Captor
//    private  ArgumentCaptor<Callback<PostModel>> createPostCallbackCaptor;
//
//    @Captor
//    private ArgumentCaptor<Callback<Void>> deletePostCallbackCaptor;
//
//
//    private PostPresenter presenter;
//
//    @Before
//    public void setUp(){
//        MockitoAnnotations.openMocks(this);
//        presenter = new PostPresenter(view, apiService);
//    }
//
//    @Test
//    public void testLoadPosts_Success(){
//        when(apiService.getPosts()).thenReturn(postCall);
//        presenter.loadPosts();
//
//        verify(view).showLoading();
//        verify(postCall).enqueue(postCallbackCaptor.capture());
//
//        List<PostModel> posts = new ArrayList<>();
//        posts.add(new PostModel());
//
//        postCallbackCaptor.getValue().onResponse(postCall, Response.success(posts));
//        verify(view).hideLoading();
//        verify(view).showPosts(posts);
//    }
//
//    @Test
//    public void testLoadPosts_Failure(){
//        when(apiService.getPosts()).thenReturn(postCall);
//        presenter.loadPosts();
//
//        verify(view).showLoading();
//        verify(postCall).enqueue(postCallbackCaptor.capture());
//
//        postCallbackCaptor.getValue().onFailure(postCall, new Throwable("Error"));
//        verify(view).hideLoading();
//        verify(view).showError("Error");
//    }
//
//    @Test
//    public void testCreatePost_Success(){
//        PostModel post = new PostModel();
//        when(apiService.postData(post)).thenReturn(createPostCall);
//        presenter.createPost(post);
//
//        verify(view).showLoading();
//        verify(createPostCall).enqueue(createPostCallbackCaptor.capture());
//
//        createPostCallbackCaptor.getValue().onResponse(createPostCall, Response.success(post));
//        verify(view).hideLoading();
//    }
//
//    @Test
//    public void testDeletePost_Success(){
//        int postId= 1;
//        when(apiService.deletePosts(postId)).thenReturn(deletePostCall);
//        presenter.deletePost(postId,8);
//
//        verify(view).showLoading();
//        verify(deletePostCall).enqueue(deletePostCallbackCaptor.capture());
//
//        deletePostCallbackCaptor.getValue().onResponse(deletePostCall, Response.success(null));
//        verify(view).hideLoading();
//
//    }
//
//}