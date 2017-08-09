package com.machine.i2max.i2max.Model;

import io.realm.RealmObject;

/**
 * Created by stories2 on 2017. 8. 9..
 */

public class RealmDouble extends RealmObject {

    public double realmDouble;

    public double getRealmDouble() {
        return realmDouble;
    }

    public void setRealmDouble(double realmDouble) {
        this.realmDouble = realmDouble;
    }

}
