package cn.looyeagee.heo.util;
/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */


import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * This sample demonstrates how to upload/download an object to/from
 * Aliyun OSS using the OSS SDK for Java.
 */
@Component

public class SimpleGetObjectSample {


    private static String endpoint;

    private static String accessKeyId;

    private static String accessKeySecret;

    private static String bucketName;

    private static String key;

    public static String upload(File f, String path,String suffix) {
        System.out.println(endpoint + accessKeyId + accessKeySecret + bucketName + key);
        OSS client = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            System.out.println(f.getName());
            System.out.println(System.currentTimeMillis());
            String fileName = System.currentTimeMillis() +suffix;
            client.putObject(new PutObjectRequest(bucketName, key + path + "/" + fileName, f));
            return "https://cdn.looyeagee.cn/heo/upload/" + path + "/" + fileName + "-heo";//todo:可能要更改路径
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message: " + oe.getErrorMessage());
            System.out.println("Error Code:       " + oe.getErrorCode());
            System.out.println("Request ID:      " + oe.getRequestId());
            System.out.println("Host ID:           " + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ce.getMessage());
        } finally {
            client.shutdown();
        }
        return null;
    }

    @Value("${oss.endpoint}")
    public void setEndpoint(String endpoint) {
        SimpleGetObjectSample.endpoint = endpoint;
    }

    @Value("${oss.accessKeyId}")
    public void setAccessKeyId(String accessKeyId) {
        SimpleGetObjectSample.accessKeyId = accessKeyId;
    }

    @Value("${oss.accessKeySecret}")
    public void setAccessKeySecret(String accessKeySecret) {
        SimpleGetObjectSample.accessKeySecret = accessKeySecret;
    }

    @Value("${oss.bucketName}")
    public void setBucketName(String bucketName) {
        SimpleGetObjectSample.bucketName = bucketName;
    }

    @Value("${oss.key}")
    public void setKey(String key) {
        SimpleGetObjectSample.key = key;
    }


}