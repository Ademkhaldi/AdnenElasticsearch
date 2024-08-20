import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { ToastrModule } from "ngx-toastr";

import { SidebarModule } from './sidebar/sidebar.module';
import { FooterModule } from './shared/footer/footer.module';
import { NavbarModule} from './shared/navbar/navbar.module';
import { FixedPluginModule} from './shared/fixedplugin/fixedplugin.module';

import { AppComponent } from './app.component';
import { AppRoutes } from './app.routing';

import { AdminLayoutComponent } from './layouts/admin-layout/admin-layout.component';
import { HttpClientModule } from "@angular/common/http";
import { ChartToDatasourceAssignmentComponent } from './CRUD/datasource/chart-to-datasource-assignment/chart-to-datasource-assignment.component';
import { PortletToChartAssignmentComponent } from './CRUD/chart/Association/portlet-to-chart-assignment/portlet-to-chart-assignment.component';
import { AppRoutingModule } from "./layouts/app-routing.module";
import { FormsModule } from "@angular/forms";
import { authInterceptorProviders } from "./USERALLL/USERALL/_helpers/auth.interceptor";
import { HomeComponent } from "./USERALLL/USERALL/home/home.component";
import { LoginComponent } from "./USERALLL/USERALL/login/login.component";
import { ProfileComponent } from "./USERALLL/USERALL/profile/profile.component";
import { RegisterComponent } from "./USERALLL/USERALL/register/register.component";
import { UpdateUserComponent } from "./USERALLL/USERALL/user/update-user/update-user.component";
import { UserListComponent } from "./USERALLL/USERALL/user/user-list/user-list.component";
import { Histogramme2Component } from './CRUD/chart/Graphiques/histogramme2/histogramme2.component';

@NgModule({
  declarations: [
    AppComponent,
    AdminLayoutComponent,
    
    
    
    LoginComponent,
    RegisterComponent,
    //UpdateUserComponent,
    HomeComponent,
    
    ProfileComponent,
         
         
         
         
         
         
         

    
    
    
  ],
  imports: [
    AppRoutingModule,  // Ajoutez le module de routage principal
    BrowserAnimationsModule,
    HttpClientModule,
    RouterModule.forRoot(AppRoutes,{
      useHash: true
    }),
    SidebarModule,
    NavbarModule,
    ToastrModule.forRoot(),
    FooterModule,
    FixedPluginModule,
    FormsModule,
    HttpClientModule,

  ],
  providers: [authInterceptorProviders],
  bootstrap: [AppComponent]
})
export class AppModule { }
