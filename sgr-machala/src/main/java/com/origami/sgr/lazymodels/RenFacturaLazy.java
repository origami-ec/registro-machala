/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sgr.lazymodels;

import com.origami.sgr.entities.RenFactura;

/**
 *
 * @author Anyelo
 */
public class RenFacturaLazy extends LazyModel<RenFactura> {

    public RenFacturaLazy() {
        super(RenFactura.class, "id", "DESC");

    }
/*
    @Override
    public void criteriaFilterSetup(CriteriaQuery crit, Map<String, FilterMeta> filters) throws Exception {
        if (filters == null) {
            filters = new HashMap<>();
        }
        List<Predicate> predicates = new ArrayList<>();
        //filters.put("estado", true);

        predicates.addAll(this.findPropertyFilter(root, filters));
        addWhere(predicates, crit);
        
    }
*/
}
