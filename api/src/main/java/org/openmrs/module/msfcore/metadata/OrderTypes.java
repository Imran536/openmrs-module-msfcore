package org.openmrs.module.msfcore.metadata;

import org.openmrs.Order;
import org.openmrs.module.metadatadeploy.descriptor.OrderTypeDescriptor;
import org.openmrs.module.msfcore.MSFCoreConfig;

public class OrderTypes {
    public static OrderTypeDescriptor REFERRAL = new OrderTypeDescriptor() {

        @Override
        public String uuid() {
            return MSFCoreConfig.REFERRAL_ORDER_TYPE_UUID;
        }

        @Override
        public String name() {
            return "Referral order type";
        }

        @Override
        public String description() {
            return "Referral's order type";
        }

        @Override
        public OrderTypeDescriptor parent() {
            return null;
        }

        @Override
        public String javaClassName() {
            return Order.class.getName();
        }
    };

}
