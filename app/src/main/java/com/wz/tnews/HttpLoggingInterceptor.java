package com.wz.tnews;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;

import android.util.Log;
import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public final class HttpLoggingInterceptor implements Interceptor {
    private static final String LOG_TAG = "HttpLoggingInterceptor";
    private static final Charset UTF8 = Charset.forName("UTF-8");
    private static final int LOGGABLE_MAX_RESPONSE_CONTENT_LENGTH = 524288;
    private volatile HttpLoggingInterceptor.Level level;

    public HttpLoggingInterceptor() {
        this.level = HttpLoggingInterceptor.Level.BASIC;
    }

    private static void log(String msgText) {
        Log.i("HttpLoggingInterceptor", msgText);
    }

    public HttpLoggingInterceptor setLevel(HttpLoggingInterceptor.Level level) {
        if(level == null) {
            throw new NullPointerException("level == null. Use Level.NONE instead.");
        } else {
            this.level = level;
            return this;
        }
    }

    public HttpLoggingInterceptor.Level getLevel() {
        return this.level;
    }

    public Response intercept(Chain chain) throws IOException {
        HttpLoggingInterceptor.Level level = this.level;
        Request request = chain.request();
        if(level == HttpLoggingInterceptor.Level.NONE) {
            return chain.proceed(request);
        } else {
            boolean logBody = level == HttpLoggingInterceptor.Level.BODY;
            boolean logHeaders = logBody || level == HttpLoggingInterceptor.Level.HEADERS;
            RequestBody requestBody = request.body();
            boolean hasRequestBody = requestBody != null;
            Connection connection = chain.connection();
            Protocol protocol = connection != null?connection.protocol():Protocol.HTTP_1_1;
            String requestStartMessage = "--> " + request.method() + ' ' + request.url() + ' ' + protocol;
            if(!logHeaders && hasRequestBody) {
                requestStartMessage = requestStartMessage + " (" + requestBody.contentLength() + "-byte body)";
            }

            log(requestStartMessage);
            if(logHeaders) {
                if(hasRequestBody) {
                    if(requestBody.contentType() != null) {
                        log("Content-Type: " + requestBody.contentType());
                    }

                    if(requestBody.contentLength() != -1L) {
                        log("Content-Length: " + requestBody.contentLength());
                    }
                }

                Headers headers = request.headers();
                int i = 0;

                for(int count = headers.size(); i < count; ++i) {
                    String name = headers.name(i);
                    if(!"Content-Type".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
                        log(name + ": " + headers.value(i));
                    }
                }

                if(logBody && hasRequestBody) {
                    if(bodyEncoded(request.headers())) {
                        log("--> END " + request.method() + " (encoded body omitted)");
                    } else {
                        Buffer buffer = new Buffer();
                        requestBody.writeTo(buffer);
                        Charset charset = UTF8;
                        MediaType contentType = requestBody.contentType();
                        if(contentType != null) {
                            charset = contentType.charset(UTF8);
                        }

                        log("");
                        if(!(requestBody instanceof MultipartBody) && isPlaintext(buffer)) {
                            log(buffer.readString(charset));
                            log("--> END " + request.method() + " (" + requestBody.contentLength() + "-byte body)");
                        } else {
                            log("--> END " + request.method() + " (binary " + requestBody.contentLength() + "-byte body omitted)");
                        }
                    }
                } else {
                    log("--> END " + request.method());
                }
            }

            long startNs = System.nanoTime();

            Response response;
            try {
                response = chain.proceed(request);
            } catch (Exception var27) {
                log("<-- HTTP FAILED: " + var27);
                throw var27;
            }

            long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);
            ResponseBody responseBody = response.body();
            long contentLength = responseBody.contentLength();
            String bodySize = contentLength != -1L?contentLength + "-byte":"unknown-length";
            log("<-- " + response.code() + ' ' + response.message() + ' ' + response.request().url() + " (" + tookMs + "ms" + (!logHeaders?", " + bodySize + " body":"") + ')');
            if(logHeaders) {
                Headers headers = response.headers();
                int i = 0;

                for(int count = headers.size(); i < count; ++i) {
                    log(headers.name(i) + ": " + headers.value(i));
                }

                if(logBody && hasBody(response)) {
                    if(bodyEncoded(response.headers())) {
                        log("<-- END HTTP (encoded body omitted)");
                    } else {
                        BufferedSource source = responseBody.source();
                        source.request(9223372036854775807L);
                        Buffer buffer = source.buffer();
                        Charset charset = UTF8;
                        MediaType contentType = responseBody.contentType();
                        if(contentType != null) {
                            try {
                                charset = contentType.charset(UTF8);
                            } catch (UnsupportedCharsetException var26) {
                                log("");
                                log("Couldn't decode the response body; charset is likely malformed.");
                                log("<-- END HTTP");
                                return response;
                            }
                        }

                        if(!isPlaintext(buffer)) {
                            log("");
                            log("<-- END HTTP (binary " + buffer.size() + "-byte body omitted)");
                            return response;
                        }

                        if(contentLength > 0L && contentLength < 524288L) {
                            log("");
                            log(buffer.clone().readString(charset));
                        }

                        log("<-- END HTTP (" + buffer.size() + "-byte body)");
                    }
                } else {
                    log("<-- END HTTP");
                }
            }

            return response;
        }
    }

    private static boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64L?buffer.size():64L;
            buffer.copyTo(prefix, 0L, byteCount);

            for(int i = 0; i < 16 && !prefix.exhausted(); ++i) {
                int codePoint = prefix.readUtf8CodePoint();
                if(Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }

            return true;
        } catch (EOFException var6) {
            return false;
        }
    }

    private static boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

    private static boolean hasBody(Response response) {
        if(response.request().method().equals("HEAD")) {
            return false;
        } else {
            int responseCode = response.code();
            return (responseCode < 100 || responseCode >= 200) && responseCode != 204 && responseCode != 304?true:contentLength(response) != -1L || "chunked".equalsIgnoreCase(response.header("Transfer-Encoding"));
        }
    }

    private static long contentLength(Response response) {
        Headers headers = response.headers();
        String s = headers.get("Content-Length");
        if(s == null) {
            return -1L;
        } else {
            try {
                return Long.parseLong(s);
            } catch (NumberFormatException var4) {
                return -1L;
            }
        }
    }

    public static enum Level {
        NONE,
        BASIC,
        HEADERS,
        BODY;

        private Level() {
        }
    }
}
