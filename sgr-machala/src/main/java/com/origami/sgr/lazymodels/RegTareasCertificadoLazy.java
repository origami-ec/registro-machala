/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.lazymodels;

import com.origami.sgr.entities.RegCertificado;

/**
 *
 * @author gutya
 */
public class RegTareasCertificadoLazy extends LazyModel<RegCertificado> {

    public RegTareasCertificadoLazy() {
        super(RegCertificado.class, "id", "DESC");

    }
/*
    @Override
    public void criteriaFilterSetup(CriteriaQuery crit, Map<String, FilterMeta> filters) throws Exception {
        if (filters == null) {
            filters = new HashMap<>();
        }
        List<Predicate> predicates = new ArrayList<>();
        //filters.put("aprobado", Boolean.FALSE);
        predicates.addAll(this.findPropertyFilter(root, filters));
        addWhere(predicates, crit);

    }*/
}
