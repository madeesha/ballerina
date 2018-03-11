/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.ballerinalang.net.http;

import org.ballerinalang.bre.Context;
import org.ballerinalang.connector.api.AbstractNativeAction;
import org.ballerinalang.connector.api.BLangConnectorSPIUtil;
import org.ballerinalang.connector.api.Resource;
import org.ballerinalang.connector.impl.ResourceImpl;
import org.ballerinalang.model.values.BConnector;
import org.ballerinalang.model.values.BMap;
import org.ballerinalang.model.values.BStruct;
import org.ballerinalang.model.values.BValue;
import org.ballerinalang.natives.AbstractNativeFunction;
import org.ballerinalang.util.codegen.PackageInfo;
import org.ballerinalang.util.codegen.ProgramFile;
import org.ballerinalang.util.codegen.ResourceInfo;
import org.ballerinalang.util.codegen.ServiceInfo;

/**
 * Utility class for websockets.
 */
public abstract class WebSocketUtil {
    public static BMap getQueryParams(Context context, AbstractNativeAction abstractNativeFunction) {
        BStruct wsConnection = (BStruct) abstractNativeFunction.getRefArgument(context, 0);
        Object queryParams = wsConnection.getNativeData(WebSocketConstants.NATIVE_DATA_QUERY_PARAMS);
        if (queryParams != null && queryParams instanceof BMap) {
            return (BMap) wsConnection.getNativeData(WebSocketConstants.NATIVE_DATA_QUERY_PARAMS);
        }
        return new BMap<>();
    }

    public static BConnector createAndGetConnector(Resource resource) {
        return BLangConnectorSPIUtil.createBConnector(getProgramFile(resource), HttpConstants.HTTP_PACKAGE_PATH,
                                                      WebSocketConstants.CONNECTOR_WEBSOCKET, new BMap<>());
    }



    public static ProgramFile getProgramFile(Resource resource) {
        return resource.getResourceInfo().getServiceInfo().getPackageInfo().getProgramFile();
    }
}
