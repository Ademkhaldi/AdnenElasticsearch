package com.example.stage7.CRUD.Dashboard.Service;

import com.example.stage7.CRUD.Dashboard.entity.Dashboard;
import com.example.stage7.CRUD.Portlet.entity.Portlet;

import java.util.List;
import java.util.Set;

public interface IDashboardService {


    List<Dashboard> getAllDashboards();



        Dashboard retrieveDashboard(String id);

    Dashboard createDashboard(Dashboard dashboard);

    Dashboard updateDashboard(String id, Dashboard dashboard);


    boolean deleteDashboard(String id);

    boolean deleteAllDashboards();

    //Affectation
    Dashboard assignerListePortletsADashboard(String id, List<Portlet> portlets);




      /*  Set<Portlet> getPortletsForDashboard(String dashboardId) ;*/
      List<Portlet> getPortletsForDashboard(String dashboardId);


    }
