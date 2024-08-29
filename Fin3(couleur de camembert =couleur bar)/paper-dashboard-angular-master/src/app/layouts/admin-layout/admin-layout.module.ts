import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AdminLayoutRoutes } from './admin-layout.routing';

import { DashboardComponent }       from '../../pages/dashboard/dashboard.component';
import { UserComponent }            from '../../pages/user/user.component';
import { TableComponent }           from '../../pages/table/table.component';
import { TypographyComponent }      from '../../pages/typography/typography.component';
import { IconsComponent }           from '../../pages/icons/icons.component';
import { MapsComponent }            from '../../pages/maps/maps.component';
import { NotificationsComponent }   from '../../pages/notifications/notifications.component';
import { UpgradeComponent }         from '../../pages/upgrade/upgrade.component';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { DashboardListComponent } from 'app/CRUD/dashboard/dashboard-list/dashboard-list.component';
import { UpdateDashboardComponent } from 'app/CRUD/dashboard/update-dashboard/update-dashboard.component';
import { AddDashboardComponent } from 'app/CRUD/dashboard/add-dashboard/add-dashboard.component';
import { DashboardDetailsComponent } from 'app/CRUD/dashboard/dashboard-details/dashboard-details.component';
import { AddPortletComponent } from 'app/CRUD/portlet/add-portlet/add-portlet.component';
import { UpdatePortletComponent } from 'app/CRUD/portlet/update-portlet/update-portlet.component';
import { PortletListComponent } from 'app/CRUD/portlet/portlet-list/portlet-list.component';
import { PortletDetailsComponent } from 'app/CRUD/portlet/portlet-details/portlet-details.component';
import { DatasourceDetailsComponent } from 'app/CRUD/datasource/datasource-details/datasource-details.component';
import { DatasourceListComponent } from 'app/CRUD/datasource/datasource-list/datasource-list.component';
import { UpdateDatasourceComponent } from 'app/CRUD/datasource/update-datasource/update-datasource.component';
import { AddChartComponent } from 'app/CRUD/chart/add-chart/add-chart.component';
import { ChartDetailsComponent } from 'app/CRUD/chart/chart-details/chart-details.component';
import { UpdateChartComponent } from 'app/CRUD/chart/update-chart/update-chart.component';
import { PortletDashboardAssignmentComponent } from 'app/CRUD/portlet/DashboarddansPortlet/portlet-dashboard-assignment/portlet-dashboard-assignment.component';
import { PortletChartAssignmentComponent } from 'app/CRUD/portlet/ChartdansPortlet/portlet-chart-assignment/portlet-chart-assignment.component';
import { ChartDatasourceAssignmentComponent } from 'app/CRUD/chart/Association/DatasourcedansChart/chart-datasource-assignment/chart-datasource-assignment.component';
import { ChartListComponent } from 'app/CRUD/chart/chart-list/chart-list.component';
import { ChartToDatasourceAssignmentComponent } from 'app/CRUD/datasource/chart-to-datasource-assignment/chart-to-datasource-assignment.component';
import { PortletToChartAssignmentComponent } from 'app/CRUD/chart/Association/portlet-to-chart-assignment/portlet-to-chart-assignment.component';
import { AssignPortletsComponent } from 'app/CRUD/dashboard/Affectation/assign-portlets/assign-portlets.component';
import { PortletsForDashboardGetComponent } from 'app/CRUD/dashboard/Affectation/portlets-for-dashboard-get/portlets-for-dashboaord-get.component';
import { UserListComponent } from 'app/USERALLL/USERALL/user/user-list/user-list.component';
import { FooterModule } from "../../shared/footer/footer.module";
import { FixedPluginModule } from "../../shared/fixedplugin/fixedplugin.module";
import { SidebarModule } from "../../sidebar/sidebar.module";
import { NavbarModule } from "../../shared/navbar/navbar.module";
import { UpdateUserComponent } from 'app/USERALLL/USERALL/user/update-user/update-user.component';
import { AddDatasourceComponent } from 'app/CRUD/datasource/add-datasource/add-datasource.component';
import { ElasticComponent } from 'app/CRUD/datasource/elastic/elastic.component';
import { Histogramme2Component } from 'app/CRUD/chart/Graphiques/histogramme2/histogramme2.component';
import { Table2Component } from 'app/CRUD/chart/Graphiques/table2/table2.component';
import { Camambert2Component } from 'app/CRUD/chart/Graphiques/Camambert2/Camambert2.component';
import { GraphiquesDashboardsComponent } from 'app/CRUD/dashboard/DashboardsGraphiques/graphiques-dashboards/graphiques-dashboards.component';
//import { AddPortletComponent } from 'app/CRUD/portlet/add-portlet/add-portlet.component';
//import { PortletDetailsComponent } from 'app/CRUD/portlet/portlet-details/portlet-details.component';
//import { UpdatePortletComponent } from 'app/CRUD/portlet/update-portlet/update-portlet.component';



@NgModule({
    declarations: [
        DashboardComponent,
        AddDashboardComponent,
        UpdateDashboardComponent,
        DashboardListComponent,
        DashboardDetailsComponent,
        AddPortletComponent,
        UpdatePortletComponent,
        PortletListComponent,
        PortletDetailsComponent,
        PortletToChartAssignmentComponent,
        DatasourceDetailsComponent,
        DatasourceListComponent,
        UpdateDatasourceComponent,
        AddDatasourceComponent,
        ChartToDatasourceAssignmentComponent,
        AddChartComponent,
        UpdateChartComponent,
        ChartDetailsComponent,
        ChartListComponent,
        PortletDashboardAssignmentComponent,
        PortletChartAssignmentComponent,
        ChartDatasourceAssignmentComponent,
        AssignPortletsComponent,
        PortletsForDashboardGetComponent,
        UpdateUserComponent, 
        UserListComponent,
        UserComponent,
        TableComponent,
        UpgradeComponent,
        TypographyComponent,
        IconsComponent,
        MapsComponent,
        ElasticComponent,
        NotificationsComponent,
        Camambert2Component,
        Histogramme2Component,
        Table2Component,
        GraphiquesDashboardsComponent
        
         

    ],
    imports: [
        CommonModule,
        RouterModule.forChild(AdminLayoutRoutes),
        FormsModule,
        NgbModule,
        FooterModule,
        FixedPluginModule,
        SidebarModule,
        NavbarModule,
        ReactiveFormsModule
    ]
})

export class AdminLayoutModule {}
