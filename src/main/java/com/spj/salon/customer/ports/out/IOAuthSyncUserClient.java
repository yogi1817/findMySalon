package com.spj.salon.customer.ports.out;

import com.spj.salon.customer.messaging.UserRegisterPayload;

public interface IOAuthSyncUserClient {
    public boolean syncAuthUser(UserRegisterPayload authUserSync);
    public boolean updatePassword(UserRegisterPayload authUserSync);
}
