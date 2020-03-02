package RestClient;

import com.newiplquizgame.myipl.pkg.UserFileUpload;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiInterface {

    @Multipart
    @POST("PostFileUpload")
    Call<UserFileUpload> uploadImage(@Part MultipartBody.Part fileToUpload, @Part MultipartBody.Part uploadedFolder);

}
