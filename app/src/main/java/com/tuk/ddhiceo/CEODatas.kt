//사업자 등록 확인에 관한 데이터 클래스 및 인터페이스
package com.tuk.ddhiceo

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query


// 사업자 등록 확인 요청을 위한 사업자 객체
//b_no: 사업자 등록번호 (10자리)
//start_dt: 개업일 (YYYYMMDD)
//p_nm: 창업주
//p_nm2: 공동 창업주
//b_nm: 상호
//corp_no: 법인 등록번호
//b_sector: 주업태명 (업종)
//b_type: 주종목명
//b_adr: 소재지
data class Business(
    val b_no: String? = "",
    val start_dt: String? = "",
    val p_nm: String? = "",
    val p_nm2: String? = "",
    val b_nm: String? = "",
    val corp_no: String? = "",
    val b_sector: String? = "",
    val b_type: String? = "",
    val b_adr: String? = ""
)


// Request Body 생성을 위한 리스트형 객체
data class BusinessRequestBodyData(val businesses: List<Business>)


// Response 의 주요 내용을 받는 객체
// valid: 01 정상 사업자
// valid: 02 등록되지 않은 사업자
data class BusinessResponseData(
    @SerializedName("b_no") val b_no: String,
    @SerializedName("valid") val valid: String,
    @SerializedName("valid_msg") val valid_msg: String,
    @SerializedName("request_param") val request_param: Any
)

data class BusinessApiResponse(
    @SerializedName("status_code") val status_code: String,
    @SerializedName("request_cnt") val request_cnt: Int,
    @SerializedName("data") val data: List<BusinessResponseData>
)


// Request by http POST method
interface BusinessApiService {
    @POST("validate")
    fun postData(
        @Query("serviceKey") serviceKey: String,
        @Query("returnType") returnType: String,
        @Body body: BusinessRequestBodyData
    ): Call<BusinessApiResponse>
}