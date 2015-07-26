package com.microstep.android.onclick.db;

import android.provider.BaseColumns;

public final class DatabaseShop {

	public static final class Shops implements BaseColumns {

        public static final String TABLE_NAME = "shops";
        public static final String DEFAULT_SORT_ORDER = "created DESC";

        /*
         * Column definitions
         */
        public static final String COLUMN_NAME_TITLE 				= "title";
        public static final String COLUMN_NAME_FINISHED 			= "finished";        
        public static final String COLUMN_NAME_CREATE_DATE 			= "created";
        public static final String COLUMN_NAME_MODIFICATION_DATE 	= "modified";
        public static final String COLUMN_NAME_TYPE 				= "type";
        public static final String COLUMN_NAME_SMS_PERSON 			= "sms_person";
        public static final String COLUMN_NAME_SYNCH 				= "synch";
        public static final String COLUMN_NAME_CATEGORY_ID 			= "category_id";
        
        public static final int COLUMN_INDEX_ID 				= 0;
        public static final int COLUMN_INDEX_TITLE 				= 1;
        public static final int COLUMN_INDEX_FINISHED 			= 2;
        public static final int COLUMN_INDEX_CREATE_DATE 		= 3;
        public static final int COLUMN_INDEX_MODIFICATION_DATE 	= 4;
        public static final int COLUMN_INDEX_TYPE 				= 5;
        public static final int COLUMN_INDEX_SMS_PERSON			= 6;
        public static final int COLUMN_INDEX_SMS_SYNCH			= 7;
        public static final int COLUMN_INDEX_SMS_CATEGORYID		= 8;
        
    }
	
	public static final class Products implements BaseColumns {

        public static final String TABLE_NAME = "products";
        public static final String DEFAULT_SORT_ORDER = "created DESC";

        /*
         * Column definitions
         */
        public static final String COLUMN_NAME_NAME 				= "name";        
        public static final String COLUMN_NAME_COUNT 				= "count";        
        public static final String COLUMN_NAME_VALUE 				= "value";
        public static final String COLUMN_NAME_SELECTED 			= "selected";
        public static final String COLUMN_NAME_SHOP_ID 				= "shop_id";
        public static final String COLUMN_NAME_CREATE_DATE 			= "created";
        public static final String COLUMN_NAME_MODIFICATION_DATE	= "modified";
        public static final String COLUMN_NAME_UNIT 				= "unit";
        public static final String COLUMN_NAME_CATEGORY_ID 			= "category_id";
        public static final String COLUMN_NAME_ORG_ID				= "organization_id";
        public static final String COLUMN_NAME_ORG_CAT_ID			= "organization_category_id";
        public static final String COLUMN_NAME_ORG_PROD_ID 			= "organization_product_id";
        public static final String COLUMN_NAME_IMG_SMALL 			= "image_small_path";
        public static final String COLUMN_NAME_IMG_NORMAL 			= "image_normal_path";
        public static final String COLUMN_NAME_DESCRIPTION 			= "description";
        public static final String COLUMN_NAME_ORG_LOCATION_ID 		= "organization_location_id";
        
        public static final int COLUMN_INDEX_ID 				= 0;
        public static final int COLUMN_INDEX_NAME 				= 1;
        public static final int COLUMN_INDEX_COUNT 				= 2;
        public static final int COLUMN_INDEX_VALUE 				= 3;
        public static final int COLUMN_INDEX_SELECTED 			= 4;
        public static final int COLUMN_INDEX_SHOP_ID 			= 5;
        public static final int COLUMN_INDEX_CREATE_DATE 		= 6;
        public static final int COLUMN_INDEX_MODIFICATION_DATE 	= 7;
        public static final int COLUMN_INDEX_UNIT 				= 8;
        public static final int COLUMN_INDEX_CATEGORY_ID 		= 9;
        public static final int COLUMN_INDEX_ORG_ID 			= 10;
        public static final int COLUMN_INDEX_ORG_CAT_ID 		= 11;
        public static final int COLUMN_INDEX_ORG_PROD_ID 		= 12;
        public static final int COLUMN_INDEX_IMG_SMALL 			= 13;
        public static final int COLUMN_INDEX_IMG_NORMAL 		= 14;
        public static final int COLUMN_INDEX_DESCRIPTION 		= 15;
        public static final int COLUMN_INDEX_ORG_LOCATION_ID	= 16;
        
    }
	
	public static final class Category implements BaseColumns {

        public static final String TABLE_NAME = "category";
        public static final String DEFAULT_SORT_ORDER = "created DESC";

        /*
         * Column definitions
         */
        public static final String COLUMN_NAME_NAME 				= "name";        
        public static final String COLUMN_NAME_LANGUAGE 			= "language";        
        public static final String COLUMN_NAME_CREATE_DATE 			= "datecreated";
        
        public static final int COLUMN_INDEX_ID 				= 0;
        public static final int COLUMN_INDEX_NAME 				= 1;
        public static final int COLUMN_INDEX_LANGUAGE 			= 2;
        public static final int COLUMN_INDEX_CREATE_DATE 		= 3;
        
    }
	
	public static final class Property implements BaseColumns {

        public static final String TABLE_NAME = "property";
        public static final String DEFAULT_SORT_ORDER = "created DESC";

        /*
         * Column definitions
         */
        public static final String COLUMN_NAME_PROPERTY				= "property";        
        public static final String COLUMN_NAME_VALUE 				= "value";        
        public static final String COLUMN_NAME_CREATE_DATE 			= "datecreated";
        public static final String COLUMN_NAME_MODIFICATION_DATE	= "modified";
        
        public static final int COLUMN_INDEX_ID 				= 0;
        public static final int COLUMN_INDEX_PROPERTY 			= 1;
        public static final int COLUMN_INDEX_VALUE 				= 2;
        public static final int COLUMN_INDEX_CREATE_DATE 		= 3;
        public static final int COLUMN_INDEX_MODIFICATION_DATE 	= 4;
        
    }
}
