package com.hungsum.framework.componments;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zhaixuan on 2017/7/21.
 */

public interface IHsLoginData extends Serializable {
    String getUserbh();

    void setUserbh(String userbh);

    String getUsername();

    void setUsername(String username);

    String getPassword();

    void setPassword(String password);

    String getRoleBhs();

    void setRoleBhs(String value);

    String getRoleMcs();

    void setRoleMcs(String value);

    String getSimpleProgressId();

    void setSimpleProgressId(String value);

    String getProgressId();

    void setProgressId(String progressId);

    void addMenu(HsMenu menu);

    HsMenus getMenus();

    void clearMenus();

    void addMenus(List<HsMenu> menus);

    List<HsRole> getRoles();


}
