/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.lazymodels;

import com.origami.sgr.entities.RenEntidadBancaria;
import com.origami.sgr.entities.RenTipoEntidadBancaria;

/**
 *
 * @author ORIGAMI2
 */
public class BancoTarjetaLazy extends LazyModel<RenEntidadBancaria> {

    private RenTipoEntidadBancaria tipo;

    public BancoTarjetaLazy() {
        super(RenEntidadBancaria.class, "id", "DESC");
    }

    public BancoTarjetaLazy(RenTipoEntidadBancaria tipo) {
        super(RenEntidadBancaria.class, "id", "DESC");
        this.tipo = tipo;
    }

//    @Override
//    public void criteriaFilterSetup(CriteriaQuery crit, Map<String, FilterMeta> filters) throws Exception {
//        if (filters == null) {
//            filters = new HashMap<>();
//        }
//        List<Predicate> predicates = new ArrayList<>();
////        filters.put("estado", true);
////        filters.put("tipo", tipo);
////        
//        predicates.addAll(this.findPropertyFilter(root, filters));
//        addWhere(predicates, crit);
//    }

}
