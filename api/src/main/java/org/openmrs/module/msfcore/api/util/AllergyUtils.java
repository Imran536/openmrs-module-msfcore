package org.openmrs.module.msfcore.api.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.openmrs.Allergen;
import org.openmrs.AllergenType;
import org.openmrs.Allergies;
import org.openmrs.Allergy;
import org.openmrs.AllergyReaction;
import org.openmrs.Concept;
import org.openmrs.Encounter;
import org.openmrs.Obs;
import org.openmrs.Patient;
import org.openmrs.api.context.Context;
import org.openmrs.module.msfcore.MSFCoreConfig;
import org.springframework.stereotype.Component;

@Component
public class AllergyUtils {

    public Allergies saveAllergies(Encounter encounter) {

        final Patient patient = encounter.getPatient();
        final List<Obs> obsGroups = this.getObservationGroups(encounter.getObsAtTopLevel(false));
        final Allergies allergies = new Allergies();

        for (final Obs obsGroup : obsGroups) {

            final Concept codedAllergenType = this.getValueCoded(obsGroup, MSFCoreConfig.ALLERGY_TYPE_UUID);
            final Concept severity = this.getValueCoded(obsGroup, MSFCoreConfig.ALLERGY_PROBLEM_SEVERITY_UUID);
            final String comment = this.getValueText(obsGroup, MSFCoreConfig.COMMENT_UUID);

            if (codedAllergenType != null) {
                final AllergenType allergenType = this.getAllergenType(codedAllergenType);
                this.addReactionsToAllergy(patient, allergies, allergenType, codedAllergenType, severity, comment, obsGroup);
            }
        }
        this.voidAllExistingPatientAllergies(patient, allergies);
        return Context.getPatientService().setAllergies(patient, allergies);
    }

    private void addReactionsToAllergy(Patient patient, Allergies allergies, AllergenType allergenType, Concept codedAllergenType,
                    Concept severity, String comment, Obs obsGroup) {

        final Optional<Obs> optionalAllergenCode = this.getAllergenCoded(allergenType, obsGroup).findAny();
        final Concept allergenCoded = optionalAllergenCode.isPresent() ? optionalAllergenCode.get().getValueCoded() : null;

        final List<AllergyReaction> reactions = new ArrayList<>();
        final Allergen allergen = new Allergen(allergenType, allergenCoded, null);
        final Allergy allergy = new Allergy(patient, allergen, severity, comment, reactions);

        this.getStream(obsGroup, MSFCoreConfig.ALLERGIC_REACTIONS_UUID).collect(Collectors.toList())
                        .forEach(o -> reactions.add(new AllergyReaction(allergy, o.getValueCoded(), o.getComment())));

        allergies.add(allergy);

    }
    private void voidAllExistingPatientAllergies(Patient patient, Allergies allergies) {
        if (!allergies.isEmpty()) {
            Context.getPatientService().getAllergies(patient).forEach(a -> Context.getPatientService().removeAllergy(a, null));
        }
    }
    private AllergenType getAllergenType(Concept codedAllergen) {

        if (MSFCoreConfig.REFERENCE_APPLICATION_COMMON_DRUG_ALLERGENS_UUID.equals(codedAllergen.getUuid())) {
            return AllergenType.DRUG;
        }
        if (MSFCoreConfig.REFERENCE_APPLICATION_COMMON_FOOD_ALLERGENS_UUID.equals(codedAllergen.getUuid())) {
            return AllergenType.FOOD;
        }
        if (MSFCoreConfig.REFERENCE_APPLICATION_COMMON_ENVIROMENTAL_ALLERGENS_UUID.equals(codedAllergen.getUuid())) {
            return AllergenType.ENVIRONMENT;
        }
        throw new IllegalArgumentException("AllergenType not found for the concept " + codedAllergen);
    }

    private Stream<Obs> getAllergenCoded(AllergenType allergenType, Obs obsGroup) {

        if (AllergenType.DRUG.equals(allergenType)) {
            return this.getStream(obsGroup, MSFCoreConfig.DRUG_ALLERGY_LIST_UUID);
        }
        if (AllergenType.FOOD.equals(allergenType)) {

            return this.getStream(obsGroup, MSFCoreConfig.FOOD_ALLERGY_LIST_UUID);
        }
        if (AllergenType.ENVIRONMENT.equals(allergenType)) {
            return this.getStream(obsGroup, MSFCoreConfig.OTHER_ALLERGY_LIST_UUID);
        }
        return null;
    }

    private Concept getValueCoded(Obs obsGroup, String conceptUuidFilter) {
        final Optional<Obs> optionalObs = this.getStream(obsGroup, conceptUuidFilter).findAny();
        return optionalObs.isPresent() ? optionalObs.get().getValueCoded() : null;
    }

    private String getValueText(Obs obsGroup, String conceptUuidFilter) {
        final Optional<Obs> optionalObs = this.getStream(obsGroup, conceptUuidFilter).findAny();
        return optionalObs.isPresent() ? optionalObs.get().getValueText() : null;
    }

    private List<Obs> getObservationGroups(Set<Obs> obsToLevel) {
        return obsToLevel.stream().filter(o -> MSFCoreConfig.ALLERGY_UUID.equals(o.getConcept().getUuid())).collect(Collectors.toList());
    }
    private Stream<Obs> getStream(Obs obsGroup, String conceptUuidFilter) {
        return obsGroup.getGroupMembers().stream().filter(o -> conceptUuidFilter.equals(o.getConcept().getUuid()));
    }
}