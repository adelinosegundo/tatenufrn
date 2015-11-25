package com.android_dev.tatenuufrn.domain;

import com.android_dev.tatenuufrn.databases.TatenUFRNDatabase;
import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.ModelContainer;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

/**
 * Created by adelinosegundo on 11/24/15.
 */

@Table(databaseName = TatenUFRNDatabase.NAME)
@ModelContainer
public class User extends BaseModel {

    @Column
    @PrimaryKey(autoincrement = false)
    private String id;

    @Column
    private String login;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
