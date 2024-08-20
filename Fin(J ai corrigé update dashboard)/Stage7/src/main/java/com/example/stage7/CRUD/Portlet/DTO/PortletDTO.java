package com.example.stage7.CRUD.Portlet.DTO;


import com.example.stage7.CRUD.BusinessEntity.BusinessEntity;
import com.example.stage7.CRUD.Chart.DTO.ChartDTO;
import com.example.stage7.CRUD.Dashboard.DTO.DashboardDTO;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PortletDTO extends BusinessEntity{

    private String row;
    private String column;

    //@JsonIgnore
    private DashboardDTO dashboard;

    private ChartDTO chart;


}
