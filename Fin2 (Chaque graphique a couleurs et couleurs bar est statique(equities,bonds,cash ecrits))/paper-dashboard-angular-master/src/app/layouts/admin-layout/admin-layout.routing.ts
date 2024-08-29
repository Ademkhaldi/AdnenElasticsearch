import { Routes } from '@angular/router';

import { DashboardComponent } from '../../pages/dashboard/dashboard.component';
import { UserComponent } from '../../pages/user/user.component';
import { TableComponent } from '../../pages/table/table.component';
import { TypographyComponent } from '../../pages/typography/typography.component';
import { IconsComponent } from '../../pages/icons/icons.component';
import { MapsComponent } from '../../pages/maps/maps.component';
import { NotificationsComponent } from '../../pages/notifications/notifications.component';
import { UpgradeComponent } from '../../pages/upgrade/upgrade.component';
import { AddDashboardComponent } from 'app/CRUD/dashboard/add-dashboard/add-dashboard.component';
import { DashboardListComponent } from 'app/CRUD/dashboard/dashboard-list/dashboard-list.component';
import { UpdateDashboardComponent } from 'app/CRUD/dashboard/update-dashboard/update-dashboard.component';
import { DashboardDetailsComponent } from 'app/CRUD/dashboard/dashboard-details/dashboard-details.component';
import { PortletListComponent } from 'app/CRUD/portlet/portlet-list/portlet-list.component';
import { AddPortletComponent } from 'app/CRUD/portlet/add-portlet/add-portlet.component';
import { UpdatePortletComponent } from 'app/CRUD/portlet/update-portlet/update-portlet.component';
import { PortletDetailsComponent } from 'app/CRUD/portlet/portlet-details/portlet-details.component';
import { DatasourceListComponent } from 'app/CRUD/datasource/datasource-list/datasource-list.component';
import { AddDatasourceComponent } from 'app/CRUD/datasource/add-datasource/add-datasource.component';
import { DatasourceDetailsComponent } from 'app/CRUD/datasource/datasource-details/datasource-details.component';
import { UpdateDatasourceComponent } from 'app/CRUD/datasource/update-datasource/update-datasource.component';
import { AddChartComponent } from 'app/CRUD/chart/add-chart/add-chart.component';
import { ChartDetailsComponent } from 'app/CRUD/chart/chart-details/chart-details.component';
import { ChartListComponent } from 'app/CRUD/chart/chart-list/chart-list.component';
import { UpdateChartComponent } from 'app/CRUD/chart/update-chart/update-chart.component';
import { PortletDashboardAssignmentComponent } from 'app/CRUD/portlet/DashboarddansPortlet/portlet-dashboard-assignment/portlet-dashboard-assignment.component';
import { PortletChartAssignmentComponent } from 'app/CRUD/portlet/ChartdansPortlet/portlet-chart-assignment/portlet-chart-assignment.component';
import { ChartDatasourceAssignmentComponent } from 'app/CRUD/chart/Association/DatasourcedansChart/chart-datasource-assignment/chart-datasource-assignment.component';
import { ChartToDatasourceAssignmentComponent } from 'app/CRUD/datasource/chart-to-datasource-assignment/chart-to-datasource-assignment.component';
import { PortletToChartAssignmentComponent } from 'app/CRUD/chart/Association/portlet-to-chart-assignment/portlet-to-chart-assignment.component';
import { AssignPortletsComponent } from 'app/CRUD/dashboard/Affectation/assign-portlets/assign-portlets.component';
import { PortletsForDashboardGetComponent } from 'app/CRUD/dashboard/Affectation/portlets-for-dashboard-get/portlets-for-dashboaord-get.component';
import { UserListComponent } from 'app/USERALLL/USERALL/user/user-list/user-list.component';
import { UpdateUserComponent } from 'app/USERALLL/USERALL/user/update-user/update-user.component';
import { ElasticComponent } from 'app/CRUD/datasource/elastic/elastic.component';
import { Histogramme2Component } from 'app/CRUD/chart/Graphiques/histogramme2/histogramme2.component';
import { Table2Component } from 'app/CRUD/chart/Graphiques/table2/table2.component';
import { Camambert2Component } from 'app/CRUD/chart/Graphiques/Camambert2/Camambert2.component';
import { GraphiquesDashboardsComponent } from 'app/CRUD/dashboard/DashboardsGraphiques/graphiques-dashboards/graphiques-dashboards.component';

export const AdminLayoutRoutes: Routes = [
    { path: 'dashboard', component: DashboardComponent },
    { path: 'getAllDashboards', component: DashboardListComponent },
    { path: 'AddDashboard', component: AddDashboardComponent },
    { path: 'UpdateDashboard/:id', component: UpdateDashboardComponent },
    { path: 'dashboard/details/:id', component: DashboardDetailsComponent },
    { path: 'dashboard/assignerListePortletsADashboard/:idDashboard', component: AssignPortletsComponent },
    { path: 'dashboard/getPortletsForDashboard/:dashboardId', component: PortletsForDashboardGetComponent },
    { path: 'dashboard/GraphiquesDashboardsComponent/:dashboardId', component: GraphiquesDashboardsComponent },

     
    
    { path: 'getAllPortlets', component: PortletListComponent },
    { path: 'AddPortlet', component: AddPortletComponent },
    { path: 'UpdatePortlet/:id', component: UpdatePortletComponent },
    { path: 'portlet/details/:id', component: PortletDetailsComponent },
    { path: 'portlet/affecterDashboardAPortlet/:idPortlet/:idDashboard', component: PortletDashboardAssignmentComponent },
    { path: 'portlet/affecterChartAPortlet/:idPortlet/:idChart', component: PortletChartAssignmentComponent },
 
   


    
    { path: 'getAllDatasources', component: DatasourceListComponent },
    { path: 'AddDatasource', component: AddDatasourceComponent },
    { path: 'UpdateDatasource/:id', component: UpdateDatasourceComponent },
    { path: 'datasource/details/:id', component: DatasourceDetailsComponent },
    { path: 'datasource/affecterChartADatasource/:idDatasource/:idChart', component: ChartToDatasourceAssignmentComponent },
    { path: 'elastic', component: ElasticComponent },


    { path: 'Camambert2Component/:id', component: Camambert2Component },

    { path: 'Table2Component/:id', component: Table2Component },



    { path: 'histogramme2/:id', component: Histogramme2Component },


    

    
    

    
    

    { path: 'getAllCharts', component: ChartListComponent },
    { path: 'AddChart', component: AddChartComponent },
    { path: 'UpdateChart/:id', component: UpdateChartComponent },
    { path: 'chart/details/:id', component: ChartDetailsComponent },
    { path: 'chart/affecterDatasourceAChart/:idChart/:idDatasource', component: ChartDatasourceAssignmentComponent },
    { path: 'chart/affecterPortletAChart/:idChart/:idPortlet', component: PortletToChartAssignmentComponent },


    
    


    


    { path: 'user-list', component: UserListComponent },{ path: 'UpdateUserComponent/:id', component: UpdateUserComponent },
    
    { path: 'user', component: UserComponent },
    { path: 'table', component: TableComponent },
    { path: 'typography', component: TypographyComponent },
    { path: 'icons', component: IconsComponent },
    { path: 'maps', component: MapsComponent },
    { path: 'notifications', component: NotificationsComponent },
    { path: 'upgrade', component: UpgradeComponent },
//    { path: '', redirectTo: 'dashboard', pathMatch: 'full' }, // Redirection vers le tableau de bord par défaut
//    { path: '**', redirectTo: 'dashboard' } //
];
