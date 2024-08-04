package com.example.stage7.CRUD.Dashboard.DTO;


import com.example.stage7.CRUD.BusinessEntity.BusinessEntity;
import com.example.stage7.CRUD.Portlet.DTO.PortletDTO;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
   public class DashboardDTO extends BusinessEntity {

    private String title;
   // @JsonIgnoreProperties("dashboard")
    private List<PortletDTO> portlets;

        /* Modifier la méthode setPortlets pour accepter une liste d'objets PortletDTO
       / public void setPortlets(List<PortletDTO> portlets) {
       /     this.portlets = portlets;
        }*/
    }




