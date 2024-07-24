package com.example.stage7.CRUD.Chart.DTO;

import com.example.stage7.CRUD.BusinessEntity.BusinessEntity;
import com.example.stage7.CRUD.Chart.entity.charttype;
import com.example.stage7.CRUD.Datasource.DTO.DatasourceDTO;
import com.example.stage7.CRUD.Portlet.DTO.PortletDTO;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChartDTO extends BusinessEntity {
    private String title;
    private charttype type;
    private String x_axis;
    private String y_axis;
    private String index;
    private DatasourceDTO datasource;
    private PortletDTO portlet;
}
