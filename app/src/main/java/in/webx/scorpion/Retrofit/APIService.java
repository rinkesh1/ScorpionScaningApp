package in.webx.scorpion.Retrofit;

import in.webx.scorpion.Model.AuthenticationInputModel;
import in.webx.scorpion.Model.BookingStockUpdate;
import in.webx.scorpion.Model.ExtraBcSerialModel;
import in.webx.scorpion.Model.ExtraDocketResponseModel;
import in.webx.scorpion.Model.GenratePRSModel;
import in.webx.scorpion.Model.GetBillingPartyListOutputModel;
import in.webx.scorpion.Model.GetBookingScanData;
import in.webx.scorpion.Model.GetBookingStockUpdate;
import in.webx.scorpion.Model.GetCityListOutputModel;
import in.webx.scorpion.Model.GetCompanyDetail;
import in.webx.scorpion.Model.GetDestinationListOutputModel;
import in.webx.scorpion.Model.GetInsertELKLog;
import in.webx.scorpion.Model.GetLodingSheetList;
import in.webx.scorpion.Model.GetPRSUpdateData;
import in.webx.scorpion.Model.GetPayBasisListInputModel;
import in.webx.scorpion.Model.GetPayBasisListOutputModel;
import in.webx.scorpion.Model.GetProductListOutputModel;
import in.webx.scorpion.Model.GetStockUpdate;
import in.webx.scorpion.Model.GetStockUpdateSelectionData;
import in.webx.scorpion.Model.GetThcDetails;
import in.webx.scorpion.Model.GetThcListForStock;
import in.webx.scorpion.Model.GetValidLoadingSheetData;
import in.webx.scorpion.Model.LS_headerRequest;
import in.webx.scorpion.Model.LS_headerResponse;
import in.webx.scorpion.Model.RequestBookingScanData;
import in.webx.scorpion.Model.RequestCompanyNameModule;
import in.webx.scorpion.Model.RequestGetLodingSheetList;
import in.webx.scorpion.Model.RequestGetStockUpdateSelectionData;
import in.webx.scorpion.Model.RequestGetThcListForStock;
import in.webx.scorpion.Model.RequestGetValidLoadingSheetData;
import in.webx.scorpion.Model.RequestInsertELKLog;
import in.webx.scorpion.Model.RequestPRSGenData;
import in.webx.scorpion.Model.RequestPRSUpdateData;
import in.webx.scorpion.Model.RmPRSHeaderModel;
import in.webx.scorpion.Model.ScanDocketDeviceDetails;
import in.webx.scorpion.Model.ScanDocketResponse;
import in.webx.scorpion.Model.ScanDocketTHCModel;
import in.webx.scorpion.Model.THCStockUpdate;
import in.webx.scorpion.Model.TokenGet;
import in.webx.scorpion.util.ConstantData;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface APIService {

    /** Get Tenant **/
    //Register API
    @POST(ConstantData.CONSTANT_VELOCITY_LOGFILE+ConstantData.GET_TENANT)
    Call<GetCompanyDetail> getcompanydetail(@Body RequestCompanyNameModule requestCompanyNameModule);

    /** Get Token **/
    //Login API
    @POST(ConstantData.BASE_AUTH_URL+ConstantData.GET_TOKEN)
    Call<TokenGet> gettoken(@Body AuthenticationInputModel loginInputModel);

    /** Get Token **/
    //Login API
    @POST(ConstantData.CONSTANT_VELOCITY_TEST_INSERTELKLOG)
    Call<GetInsertELKLog> getInsertELKLog(@Body RequestInsertELKLog requestInsertELKLog);

    /** Outward APIs **/
    //LS List API
    @POST(ConstantData.BASE_URL+ConstantData.LS_LIST)
    Call<GetLodingSheetList> getloadingsheetlist(@Body RequestGetLodingSheetList getLodingSheetList);

    //Docket & Barcode List API
    @POST(ConstantData.BASE_URL+ConstantData.LS_DATA)
    Call<GetValidLoadingSheetData> getvalidloadingsheetdata(@Body RequestGetValidLoadingSheetData requestLoadingSheetData);

    //Manifest Generation API
    @POST(ConstantData.BASE_URL+ConstantData.LS_MANIFESTGENERATION)
    Call<LS_headerResponse> manifestgeneration(@Body LS_headerRequest manifestRequest);

    //Dcoket Generation API
    @POST(ConstantData.BASE_URL+ConstantData.LS_PACKAGE_UPDATE)
    Call<ScanDocketResponse> packageUpdate(@Body ScanDocketDeviceDetails docketUpdateScan);

    @POST(ConstantData.BASE_URL+ConstantData.THC_DOCKET_UPDATE)
    Call<ScanDocketResponse> packageTHCUpdate(@Body ScanDocketTHCModel docketUpdateScan);

    @POST(ConstantData.BASE_URL+ConstantData.LS_EXTRA_PKG_UPDATE)
    Call<ExtraDocketResponseModel> extraBarcodeUpdate(@Body ExtraBcSerialModel docketUpdateScan);

    /** Inward APIs **/
    //GetThcListForStock API
    @POST(ConstantData.BASE_URL+ConstantData.THC_LIST)
    Call<GetThcListForStock> getthclistforstock(@Body RequestGetThcListForStock requestGetThcListForStock);

    /** Inward APIs **/
    //GetThcListForStock API
    @POST(ConstantData.BASE_URL+ConstantData.THC_REASON_LIST)
    Call<GetStockUpdateSelectionData> getStockUpdateSelectionData(@Body RequestGetStockUpdateSelectionData requestGetStockUpdateSelectionData);

    /** Inward APIs **/
    //GetThcDetails API
    @POST(ConstantData.BASE_URL+ConstantData.THC_Docket_LIST)
    Call<GetThcDetails> getThcDetails(@Body RequestGetStockUpdateSelectionData requestGetStockUpdateSelectionData);

    /** Inward APIs **/
    //GetThcDetails API
    @POST(ConstantData.BASE_URL+ConstantData.THC_STOCK_UPDATE)
    Call<GetStockUpdate> getstockUpdate(@Body THCStockUpdate thcStockUpdate);

    //Generate PRS
    @POST(ConstantData.BASE_URL+ConstantData.PRS_GENERATION)
    Call<GenratePRSModel> getPRSgenration(@Body RmPRSHeaderModel generatePRS);

    // GET PRS GEN SUMMARY DATA
    @POST(ConstantData.BASE_URL+ConstantData.PRS_DOCKET_LIST)
    Call<RmPRSHeaderModel> getPRSGendataList(@Body RequestPRSGenData request);

    // GET PRS UPDATE SUMMARY DATA
    @POST(ConstantData.BASE_URL+ConstantData.LS_DATA)
    Call<GetPRSUpdateData> getPRSUpdatedata(@Body RequestPRSUpdateData request);

    // GET BOOKING SCAN DATA
    @POST(ConstantData.BASE_URL+ConstantData.BOOKING_DATA)
    Call<GetBookingScanData> getBookingScandata(@Body RequestBookingScanData request);

    // SAVE BOOKING SCAN DATA
    @POST(ConstantData.BASE_URL+ConstantData.BOOKING_DATA_SAVE)
    Call<GetBookingStockUpdate> getBookedScandata(@Body BookingStockUpdate request);

    // PAYMENTBASE DATA
    @POST(/*ConstantData.BASE_URL+*/ConstantData.QB_PAYMENTBASE_DATA)
    Call<GetPayBasisListOutputModel> getPayBasisList(@Body GetPayBasisListInputModel request);


    // BILLINGPARTY DATA
    @POST(/*ConstantData.BASE_URL+*/ConstantData.QB_BILLINGPARTY_DATA)
    Call<GetBillingPartyListOutputModel> getBillingParty(@Body GetPayBasisListInputModel request);

    // LOCATION DATA
    @POST(/*ConstantData.BASE_URL+*/ConstantData.QB_DESTINATION_DATA)
    Call<GetDestinationListOutputModel> getDestination(@Body GetPayBasisListInputModel request);

    // CITY DATA
    @POST(/*ConstantData.BASE_URL+*/ConstantData.QB_CITY_DATA)
    Call<GetCityListOutputModel> getCityList(@Body GetPayBasisListInputModel request);

    // CITY DATA
    @POST(/*ConstantData.BASE_URL+*/ConstantData.QB_PRODUCT_DATA)
    Call<GetProductListOutputModel> getProductList(@Body GetPayBasisListInputModel request);

}
