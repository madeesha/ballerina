/*
 * Copyright (c) 2018, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

package org.ballerinalang.net.jms.nativeimpl.endpoint.queue.consumer;

import org.ballerinalang.bre.Context;
import org.ballerinalang.bre.bvm.CallableUnitCallback;
import org.ballerinalang.connector.api.BLangConnectorSPIUtil;
import org.ballerinalang.connector.api.Struct;
import org.ballerinalang.model.NativeCallableUnit;
import org.ballerinalang.model.types.TypeKind;
import org.ballerinalang.model.values.BStruct;
import org.ballerinalang.natives.annotations.Argument;
import org.ballerinalang.natives.annotations.BallerinaFunction;
import org.ballerinalang.natives.annotations.Receiver;
import org.ballerinalang.net.jms.Constants;

import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.Queue;
import javax.jms.Session;

/**
 * Create JMS consumer for a consumer endpoint.
 *
 * @since 0.970
 */

@BallerinaFunction(
        orgName = "ballerina", packageName = "jms",
        functionName = "createConsumer",
        receiver = @Receiver(type = TypeKind.STRUCT, structType = "QueueConsumer", structPackage = "ballerina.jms"),
        args = {@Argument(name = "connector", type = TypeKind.STRUCT, structType = "SessionConnector")
        },
        isPublic = true
)
public class CreateConsumer implements NativeCallableUnit {
    @Override
    public void execute(Context context, CallableUnitCallback callableUnitCallback) {
        Struct consumerEndpoint = BLangConnectorSPIUtil.getConnectorEndpointStruct(context);
        BStruct sessionConnector = (BStruct) context.getRefArgument(1);
        Object sessionData = sessionConnector.getNativeData(Constants.JMS_SESSION);
        Struct consumerConfig = consumerEndpoint.getStructField(Constants.CONSUMER_CONFIG);
        String queueName = consumerConfig.getStringField(Constants.QUEUE_NAME);

        if (sessionData instanceof Session) {
            Session session = (Session) sessionData;
            try {
                Queue queue = session.createQueue(queueName);
                MessageConsumer consumer = session.createConsumer(queue);
                Struct consumerConnector = consumerEndpoint.getStructField(Constants.CONSUMER_CONNECTOR);
                consumerConnector.addNativeData(Constants.JMS_CONSUMER_OBJECT, consumer);
            } catch (JMSException e) {
                // ignore
            }
        }
    }

    @Override
    public boolean isBlocking() {
        return true;
    }
}
