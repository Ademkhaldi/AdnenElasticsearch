package com.example.stage7.CRUD.Datasource.DTO;

import com.example.stage7.CRUD.BusinessEntity.BusinessEntity;
import com.example.stage7.CRUD.Chart.DTO.ChartDTO;
import com.example.stage7.CRUD.Chart.entity.Chart;
import com.example.stage7.CRUD.Portlet.DTO.PortletDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DatasourceDTO extends BusinessEntity {
    private String type;
    private Integer connection_port;
    private String url;
    private String user;
    private String password;



    private List<ChartDTO> charts;



}
