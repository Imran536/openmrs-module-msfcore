/**
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 * 
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.msfcore.api.dao;

import static org.hibernate.criterion.Order.desc;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.openmrs.Concept;
import org.openmrs.ConceptAnswer;
import org.openmrs.Drug;
import org.openmrs.DrugOrder;
import org.openmrs.Location;
import org.openmrs.LocationAttribute;
import org.openmrs.LocationAttributeType;
import org.openmrs.Obs;
import org.openmrs.Order;
import org.openmrs.Order.Action;
import org.openmrs.OrderType;
import org.openmrs.Patient;
import org.openmrs.PatientIdentifier;
import org.openmrs.PatientIdentifierType;
import org.openmrs.Person;
import org.openmrs.PersonAttribute;
import org.openmrs.PersonAttributeType;
import org.openmrs.api.APIException;
import org.openmrs.api.context.Context;
import org.openmrs.api.db.hibernate.DbSession;
import org.openmrs.api.db.hibernate.DbSessionFactory;
import org.openmrs.module.idgen.SequentialIdentifierGenerator;
import org.openmrs.module.msfcore.Pagination;
import org.springframework.stereotype.Repository;

@Repository("msfcore.MSFCoreDao")
public class MSFCoreDao extends MSFCoreBaseDao {

    private DbSession getSession() {
        return Context.getRegisteredComponents(DbSessionFactory.class).get(0).getCurrentSession();
    }

    @SuppressWarnings("unchecked")
    public List<Concept> getAllConceptAnswers(Concept question) {
        List<Concept> answers = null;
        if (question != null && question.getDatatype().isCoded()) {
            answers = new ArrayList<Concept>();
            for (ConceptAnswer answer : (List<ConceptAnswer>) getSession().createCriteria(ConceptAnswer.class).add(
                            Restrictions.eq("concept", question)).list()) {
                answers.add(answer.getAnswerConcept());
            }
        }
        return answers;
    }

    @SuppressWarnings("unchecked")
    public List<LocationAttribute> getLocationAttributeByTypeAndLocation(LocationAttributeType type, Location location) {
        return getSession().createCriteria(LocationAttribute.class).add(Restrictions.eq("location", location)).add(
                        Restrictions.eq("attributeType", type)).addOrder(desc("dateCreated")).list();
    }

    public void saveSequencyPrefix(SequentialIdentifierGenerator generator) {
        int updated = getSession().createSQLQuery("update idgen_seq_id_gen set prefix = :val where id = :id").setParameter("val",
                        generator.getPrefix()).setParameter("id", generator.getId()).executeUpdate();
        if (updated != 1) {
            throw new APIException("Expected to update 1 row but updated " + updated + " rows instead!");
        }
    }

    @SuppressWarnings("unchecked")
    public List<PersonAttribute> getPersonAttributeByTypeAndPerson(PersonAttributeType type, Person person) {
        return getSession().createCriteria(PersonAttribute.class).add(Restrictions.eq("person", person)).add(
                        Restrictions.eq("attributeType", type)).addOrder(desc("dateCreated")).list();
    }

    @SuppressWarnings("unchecked")
    public List<PatientIdentifier> getPatientIdentifierByTypeAndPatient(PatientIdentifierType type, Patient patient) {
        return getSession().createCriteria(PatientIdentifier.class).add(Restrictions.eq("patient", patient)).add(
                        Restrictions.eq("identifierType", type)).addOrder(desc("dateCreated")).list();
    }

    @SuppressWarnings("unchecked")
    public List<Order> getOrders(Patient patient, OrderType type, List<Concept> concepts, Pagination pagination) {
        Criteria crit = getSession().createCriteria(Order.class);
        if (type != null) {
            crit.add(Restrictions.eq("orderType", type));
        }
        if (patient != null) {
            crit.add(Restrictions.eq("patient", patient));
        }
        if (concepts != null && !concepts.isEmpty()) {
            crit.add(Restrictions.in("concept", concepts));
        }
        // exclude discontinuing orders
        crit.add(Restrictions.not(Restrictions.eq("action", Action.DISCONTINUE)));

        applyPaginationToCriteria(pagination, crit);
        crit.addOrder(desc("dateActivated"));

        return crit.list();
    }

    @SuppressWarnings("unchecked")
    public List<Obs> getObservationsByOrderAndConcept(Order order, Concept concept) {
        Criteria crit = getSession().createCriteria(Obs.class);
        if (order != null) {
            crit.add(Restrictions.eq("order", order));
        }
        if (concept != null) {
            crit.add(Restrictions.eq("concept", concept));
        }
        return crit.list();
    }

    public DrugOrder getDrugOrder(Integer id) {
        return (DrugOrder) getSession().get(DrugOrder.class, id);
    }

    public Drug getDrugByConcept(Concept medication) {
        return (Drug) getSession().createCriteria(Drug.class).add(Restrictions.eq("concept", medication)).uniqueResult();
    }
}
