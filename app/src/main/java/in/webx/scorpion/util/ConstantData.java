package in.webx.scorpion.util;

public class ConstantData {

    // ----------------Shared Prefrences---------------

    public static final String SP_FILE_NAME = "VelocityAndroid";
    public static final String SP_COMPANY_STATUS = "Companystatus";
    public static final String SP_COMPANY_CODE = "CompanyCode";
    public static final String SP_COMPANY_IMAGE = "CompanyImage";
    public static final String SP_COMPANY_NAME = "CompanyName";
    public static final String SP_REMEMBER = "Remember";
    public static final String SP_KEY_USERNAME = "Username";
    public static final String READ_FAIL = "READ_FAIL";
    public static final String SP_KEY_PASSWORD = "Password";
    public static final String SP_KEY_BIKER_NAME = "Bikername";
    public static final String SP_KEY_BRANCH_CODE = "BranchCode";
    public static final String SP_KEY_ISPARTIALSCAN = "IsPartialScan";
    public static final String SP_KEY_ISSELECTMENU = "IsSelectMenu";
    public static final String SP_LS_NO = "LsNo";
    public static final String SP_TOTAL_PACKAGES = "TotalPackages";
    public static final String SP_TOTAL_DOCKETS = "TotalDockets";
    public static final String SP_THC_NO = "ThcNo";
    public static final String SP_KEY_EMP_ID = "employeeId";
    public static final String SP_KEY_TOKEN = "token";
    public static final String SP_KEY_TOKEN_EXPIRY = "token_expiry";
    public static final String SP_KEY_RULES = "rules";
    public static final String SP_KEY_DOCKET_NO="DocketNo";
    public static final String SP_KEY_VEHICLE_NO="VehicleNo";
    public static final String SP_KEY_LOCATION_LAT = "LAT";
    public static final String SP_KEY_LOCATION_LNG = "LNG";
    public static final String SP_KEY_IS_SUBMITTED= "IsChecked";
    public static final String SP_KEY_COMPANY_CODE = "companyCode";
    public static final String SP_KEY_COMPANY_NAME = "companY_NAME";
    public static final String SP_KEY_COMPANY_LOGO = "companY_LOGO";
    public static final String SP_KEY_IsPushGPSDataEnable = "IsPushGPSDataEnable";
    public static final String SP_KEY_FCMToken = "fgb6fwT07PLZaCg8aFRKXN:APA91bFXjSnsN26zEY1VBu0QGygf1bIW_IaNw0O0yCmPASua5aeFeo-8xfsKB7GFmOqr1L6ASuvcSYtCety6GUHyo9X3b0M42inANQBh66fxjr2WGVYtNdOmHS04PutqcjBc4IS3XCwO";
    public static final String SP_KEY_AuthType = "USER";
    public static final String SP_KEY_VEHICLENO = "vehicleNo";
    public static final String SP_KEY_EXTRA = "Extra";
    public static final String SP_KEY_DAMAGE = "Damage";
    public static final String SP_KEY_INWARD = "Inward";

    public static final String SP_ArrivalCondition = "ArrivalCondition";
    public static final String SP_GoDown = "GoDown";
    public static final String SP_SpDEPS = "SpDEPS";
    public static final String SP_SpDeliveryType = "SpDeliveryType";
    public static final String SP_SpDEPSReason = "SpDEPSReason";

    public static final String CUSTOMER_ID = "CustomerId";

    public static final String CONST_LOG_FILE = CommonMethod.getCurrentDate()+"_"+"webXvelocity.jpeg";

    public static final String CONSTANT_VELOCITY_TEST_INSERTELKLOG = "https://webxservices-velocity.azurewebsites.net/api/ELK/InsertELKLog";

    /*---------------------------Scorpion LIVE---------------------------------*/

    public static final String CONSTANT_VELOCITY_LOGFILE = "https://sgtms-webx-services-auth.azurewebsites.net/api/Authenticate/";
    public static final String CONSTANT_DTDC_LIVE_AUTH = "https://sgtms-webx-services-auth.azurewebsites.net/api/Authenticate/";
    public static final String CONSTANT_DTDC_LIVE_URL_REST = "https://sgtms-webx-services-ops.azurewebsites.net/api/Operations/";

    public static final String GET_TENANT = "GetCompanyDetailV1";
    public static final String GET_TOKEN = "GetToken";
    /*------------------------------------------------------------*/

    /*---------------------------Scorpion BETA---------------------------------*/

//    public static final String CONSTANT_VELOCITY_LOGFILE = "https://sgtms-webx-services-auth-beta.azurewebsites.net/api/Authenticate/";
//    public static final String CONSTANT_DTDC_LIVE_AUTH = "https://sgtms-webx-services-auth-beta.azurewebsites.net/api/Authenticate/";
//    public static final String CONSTANT_DTDC_LIVE_URL_REST = "https://sgtms-webx-services-ops-beta.azurewebsites.net/api/Operations/";
//
//    public static final String GET_TENANT = "GetCompanyDetailV1";
//    public static final String GET_TOKEN = "GetToken";

    /*-----------------------------BETA-------------------------------*/

//      public static final String AppType = "TEST";
    public static final String AppType = "LIVE";

    public static final String BASE_AUTH_URL = CONSTANT_DTDC_LIVE_AUTH;
    public static final String BASE_URL = CONSTANT_DTDC_LIVE_URL_REST;

    // ----------------Authentication---------------

//    public static final String GET_TENANT = "GetCompanyDetail";
//    public static final String GET_TOKEN = "GetToken";

    // -------------------Outward-------------------

    public static final String LS_LIST = "LoadingSheetFuncations/GetLodingSheetList";
    public static final String LS_DATA = "LoadingSheetFuncations/GetValidLoadingSheetData";
    public static final String LS_MANIFESTGENERATION = "LoadingSheetFuncations/ManifestGenerationV2";
    public static final String LS_PACKAGE_UPDATE = "LoadingSheetFuncations/LSUpdateDetails";
    public static final String LS_EXTRA_PKG_UPDATE = "LoadingSheetFuncations/GetExtraBarcodeDetail";

    //--------------------Inward-------------------

    public static final String THC_LIST = "THCFuncations/GetThcListForStock";
    public static final String THC_REASON_LIST = "StockUpdateFunctions/GetStockUpdateSelectionData";
    public static final String THC_Docket_LIST = "THCFuncations/GetThcDetails";
    public static final String THC_STOCK_UPDATE = "StockUpdateFunctions/StockUpdateV2";
    public static final String THC_DOCKET_UPDATE = "StockUpdateFunctions/StockUpdateDetails";

    //--------------------PRS GENERATION-------------------
    public static final String PRS_DOCKET_LIST = "PRSFuncations/DocketListForPRS";
    public static final String PRS_GENERATION = "PRSFuncations/GeneratePRS";

    //--------------------Device------------------
    public static final String UROVO = "UBX DT50Q--Brand Urovo";
    public static final String Honeywell = "Honeywell EDA52--Brand Honeywell";
    public static final String NEWLAND = "Newland NLS-MT90--Brand Newland";
    public static final String SCANNER_PM80 = "PM80";
    public static final String SCANNER_PM85 = "PM85";
    public static final String SCANNER_PM75 = "PM75";

    //--------------------Service------------------
    public static final String REFRESH_INTERVEL_LAT_LON = "1";
    public static final String CHANNEL_ID_FOR_UPLOAD = "Data Upload";
    public static final String CHANNEL_NAME_FOR_UPLOAD = "Data Upload Channel";
    public static int NOTIFICATION_ID_FOR_UPLOAD = 1002;
    public static final String CHANNEL_ID_FOR_LOCATION = "Location Update";
    public static final String CHANNEL_NAME_FOR_LOCATION = "Location Update Channel";
    public static int NOTIFICATION_ID_FOR_LOCATION = 875426;

    public interface ACTION {
        public static String STARTFOREGROUND_ACTION = "startforeground";
        public static String STOPFOREGROUND_ACTION = "stopforeground";
    }

    //--------------------Massage------------------

    public static final String GRANT_PERMISSION = "Please grant the permission to continue";

    // ------------------Booking--------------------
    public static final String BOOKING_DATA ="BookingScanFuncations/DocketListForScan";
    public static final String BOOKING_DATA_SAVE ="BookingScanFuncations/BookingScanSaveDetails";

    //------------------QUICKBOOKING------------------
    public static final String QB_PAYMENTBASE_DATA ="https://c003.beta.mywebxpress.com/RestService/OperationService.svc/GetPayBasisList";
    public static final String QB_BILLINGPARTY_DATA ="https://c003.beta.mywebxpress.com/RestService/OperationService.svc/GetCustomerListNew";
    public static final String QB_DESTINATION_DATA ="https://c003.beta.mywebxpress.com/RestService/OperationService.svc/GetLocationListNew";
    public static final String QB_CITY_DATA ="https://c003.beta.mywebxpress.com/RestService/OperationService.svc/GetCityListNew";
    public static final String QB_PRODUCT_DATA ="https://c003.beta.mywebxpress.com/RestService/OperationService.svc/GetProductList";
}
