package com.spj.salon.customer.ports.out;

import com.spj.salon.customer.messaging.UserRegisterPayload;

public interface IOAuthSyncUserClient {
    boolean syncAuthUser(UserRegisterPayload authUserSync);
    boolean updatePassword(UserRegisterPayload authUserSync);
}
