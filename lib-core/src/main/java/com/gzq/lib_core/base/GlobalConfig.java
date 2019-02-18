package com.gzq.lib_core.base;

import android.text.TextUtils;

import com.gzq.lib_core.base.config.CrashManagerConfig;
import com.gzq.lib_core.base.config.GsonConfig;
import com.gzq.lib_core.base.config.OkhttpConfig;
import com.gzq.lib_core.base.config.RetrofitConfig;
import com.gzq.lib_core.base.config.RoomDatabaseConfig;
import com.gzq.lib_core.base.config.SessionManagerConfig;
import com.gzq.lib_core.http.cache.CacheMode;

import me.jessyan.autosize.unit.Subunits;


public final class GlobalConfig {
    private String baseUrl;
    private boolean canMock;
    private String roomName;
    private int designWidth;
    private int designHeight;
    private boolean isSupportDP;
    private boolean isSupportSP;
    private Subunits subunits;
    /**
     * 默认Room进行网络缓存
     */
    private boolean isRoomCache;
    private String cacheMode;
    private int cacheSecond;
    private GsonConfig gsonConfig;
    private OkhttpConfig okhttpConfig;
    private RetrofitConfig retrofitConfig;
    private RoomDatabaseConfig roomDatabaseConfig;
    private SessionManagerConfig sessionManagerConfig;
    private CrashManagerConfig crashManagerConfig;

    public String getBaseUrl() {
        return baseUrl;
    }

    public boolean isCanMock() {
        return canMock;
    }

    public String getRoomName() {
        return roomName;
    }

    public int getDesignWidth() {
        return designWidth;
    }

    public int getDesignHeight() {
        return designHeight;
    }

    public boolean isSupportDP() {
        return isSupportDP;
    }

    public boolean isSupportSP() {
        return isSupportSP;
    }

    public Subunits getSubunits() {
        return subunits;
    }

    public boolean isRoomCache() {
        return isRoomCache;
    }

    public String getCacheMode() {
        return cacheMode;
    }

    public int getGlobalCacheSecond() {
        return cacheSecond;
    }

    public GsonConfig getGsonConfig() {
        return gsonConfig;
    }

    public OkhttpConfig getOkhttpConfig() {
        return okhttpConfig;
    }

    public RetrofitConfig getRetrofitConfig() {
        return retrofitConfig;
    }

    public RoomDatabaseConfig getRoomDatabaseConfig() {
        return roomDatabaseConfig;
    }

    public SessionManagerConfig getSessionManagerConfig() {
        return sessionManagerConfig;
    }

    public CrashManagerConfig getCrashManagerConfig() {
        return crashManagerConfig;
    }

    public GlobalConfig(Builder builder) {
        this.baseUrl = builder.baseUrl;
        this.canMock = builder.canMock;
        this.roomName = builder.roomName;
        this.designWidth = builder.designWidth;
        this.designHeight = builder.designHeight;
        this.isSupportSP = builder.isSupportSP;
        this.isSupportDP = builder.isSupportDP;
        this.subunits = builder.subunits;
        this.isRoomCache = builder.isRoomCache;
        this.cacheMode = builder.cacheMode;
        this.cacheSecond = builder.cacheSecond;
        this.gsonConfig = builder.gsonConfig;
        this.okhttpConfig = builder.okhttpConfig;
        this.retrofitConfig = builder.retrofitConfig;
        this.roomDatabaseConfig = builder.roomDatabaseConfig;
        this.sessionManagerConfig = builder.sessionManagerConfig;
        this.crashManagerConfig = builder.crashManagerConfig;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String baseUrl;
        private boolean canMock;
        private String roomName;
        private int designWidth;
        private int designHeight;
        private boolean isSupportDP;
        private boolean isSupportSP;
        private Subunits subunits;
        private boolean isRoomCache = true;
        private String cacheMode = CacheMode.NO_CACHE;
        private int cacheSecond = -1;
        private GsonConfig gsonConfig;
        private OkhttpConfig okhttpConfig;
        private RetrofitConfig retrofitConfig;
        private RoomDatabaseConfig roomDatabaseConfig;
        private SessionManagerConfig sessionManagerConfig;
        private CrashManagerConfig crashManagerConfig;


        private Builder() {
        }

        public Builder baseurl(String baseUrl) {//基础url
            if (TextUtils.isEmpty(baseUrl)) {
                throw new IllegalArgumentException("BaseUrl can not be empty");
            }
            this.baseUrl = baseUrl;
            return this;
        }

        public Builder canMock(boolean canMock) {
            this.canMock = canMock;
            return this;
        }

        public Builder roomName(String roomName) {
            if (TextUtils.isEmpty(roomName)) {
                throw new IllegalArgumentException("RoomDatabase name can not be empty");
            }
            this.roomName = roomName;
            return this;
        }

        public Builder designWidth(int width) {
            this.designWidth = width;
            return this;
        }

        public Builder designHeight(int height) {
            this.designHeight = height;
            return this;
        }

        public Builder autoSize(boolean isSupportSP, boolean isSupportDP, Subunits subunits) {
            this.isSupportSP = isSupportSP;
            this.isSupportDP = isSupportDP;
            this.subunits = subunits;
            return this;
        }

        public Builder roomCache(boolean isRoomCache, String cacheMode, int cacheSecond) {
            this.cacheSecond = cacheSecond;
            if (isRoomCache) {
                this.isRoomCache = true;
                this.cacheMode = cacheMode;
            } else {
                this.isRoomCache = false;
                this.cacheMode = CacheMode.DEFAULT;
            }
            return this;
        }

        public Builder gsonConfiguration(GsonConfig gsonConfiguration) {
            this.gsonConfig = gsonConfiguration;
            return this;
        }

        public Builder okhttpConfiguration(OkhttpConfig okhttpConfiguration) {
            this.okhttpConfig = okhttpConfiguration;
            return this;
        }

        public Builder retrofitConfiguration(RetrofitConfig retrofitConfiguration) {
            this.retrofitConfig = retrofitConfiguration;
            return this;
        }

        public Builder roomDatabaseConfiguration(RoomDatabaseConfig roomDatabaseConfiguration) {
            this.roomDatabaseConfig = roomDatabaseConfiguration;
            return this;
        }

        public Builder sessionManagerConfiguration(SessionManagerConfig sessionManagerConfiguration) {
            this.sessionManagerConfig = sessionManagerConfiguration;
            return this;
        }

        public Builder crashManagerConfiguration(CrashManagerConfig crashManagerConfiguration) {
            this.crashManagerConfig = crashManagerConfiguration;
            return this;
        }

        GlobalConfig build() {
            return new GlobalConfig(this);
        }
    }
}
