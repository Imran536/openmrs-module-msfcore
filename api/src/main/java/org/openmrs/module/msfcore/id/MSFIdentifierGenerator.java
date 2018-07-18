package org.openmrs.module.msfcore.id;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.openmrs.Location;
import org.openmrs.LocationAttribute;
import org.openmrs.PatientIdentifierType;
import org.openmrs.api.context.Context;
import org.openmrs.module.idgen.AutoGenerationOption;
import org.openmrs.module.idgen.SequentialIdentifierGenerator;
import org.openmrs.module.idgen.service.IdentifierSourceService;
import org.openmrs.module.msfcore.MSFCoreConfig;
import org.openmrs.module.msfcore.api.MSFCoreService;

/**
 * MSF ID generator, format: UK-AB18.06.026 where UK is a country's code and AB
 * location initial/code, 18 year(yy), 06 month(MM) and 026 is the increasing
 * number
 */
public class MSFIdentifierGenerator extends SequentialIdentifierGenerator {

  private String locationCode;
  private String countryCode;
  private Date date;

  public MSFIdentifierGenerator() {
    setBasicProperties();
  }

  private void setBasicProperties() {
    evaluatePrefix();
    setName("MSF ID Source");
    setUuid(MSFCoreConfig.PATIENT_ID_TYPE_SOURCE_MSF_UUID);
    setDescription("Used to generate MSF default patient indentifiers");
    setBaseCharacterSet(MSFCoreConfig.MSF_ID_BASE_CHARACTER_SET);
    setRetired(false);
    setFirstIdentifierBase("1");
    setSuffix("");
  }

  public MSFIdentifierGenerator(String countryCode, String locationCode) {
    setLocationCode(locationCode);
    setCountryCode(countryCode);
    setBasicProperties();
  }

  public MSFIdentifierGenerator(String countryCode, String locationCode, Date date) {
    setLocationCode(locationCode);
    setCountryCode(countryCode);
    setDate(date);
    setBasicProperties();
  }

  public String getLocationCode() {
    if (StringUtils.isBlank(locationCode)) {
      locationCode = "MSF";
      Location defaultLocation = Context.getLocationService().getLocation(
          Context.getAdministrationService().getGlobalProperty(MSFCoreConfig.GP_DEFAULT_LOCATION));

      if (defaultLocation != null) {
        List<LocationAttribute> codes = Context.getService(MSFCoreService.class)
            .getLocationAttributeByTypeAndLocation(Context.getLocationService()
                .getLocationAttributeTypeByUuid(MSFCoreConfig.LOCATION_ATTR_TYPE_CODE_UUID),
                defaultLocation);
        if (codes.size() > 0) {
          //TODO test this toString
          locationCode = codes.get(0).getValue().toString();
        }
      }
    }
    return locationCode;
  }

  public void setLocationCode(String locationCode) {
    this.locationCode = locationCode;
  }

  public String getCountryCode() {
    if (StringUtils.isBlank(countryCode)) {
      countryCode = Context.getAdministrationService().getGlobalProperty(MSFCoreConfig.GP_COUNTRY_CODE);
    }
    return countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  public Date getDate() {
    if (date == null) {
      date = new Date();
    }
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  /**
   * Format: UK-AB18.06.
   */
  public String msfPrefix() {
    return getCountryCode() + "-" + getLocationCode() + new SimpleDateFormat("yy.MM.").format(getDate());
  }

  public void evaluatePrefix() {
    setPrefix(msfPrefix());
  }

  private void install() {
    SequentialIdentifierGenerator msfIdGenerator = tranformType(this);
    PatientIdentifierType msfIdType = Context.getPatientService()
        .getPatientIdentifierTypeByUuid(MSFCoreConfig.PATIENT_ID_TYPE_MSF_UUID);

    msfIdGenerator.setIdentifierType(msfIdType);
    SequentialIdentifierGenerator retrivedMsfIdGenerator = (SequentialIdentifierGenerator) Context
        .getService(IdentifierSourceService.class)
        .getIdentifierSourceByUuid(MSFCoreConfig.PATIENT_ID_TYPE_SOURCE_MSF_UUID);
    // reset next sequency if dynamic prefix changes
    if (retrivedMsfIdGenerator != null && !retrivedMsfIdGenerator.getPrefix().equals(msfIdGenerator.getPrefix())) {
      Context.getService(IdentifierSourceService.class).saveSequenceValue(retrivedMsfIdGenerator, 1L);
      retrivedMsfIdGenerator.setPrefix(msfIdGenerator.getPrefix());
      Context.getService(IdentifierSourceService.class).saveIdentifierSource(retrivedMsfIdGenerator);
    } else {
      Context.getService(IdentifierSourceService.class).saveIdentifierSource(msfIdGenerator);
    }

    AutoGenerationOption option = Context.getService(IdentifierSourceService.class)
        .getAutoGenerationOption(msfIdType);
    if (option == null) {
      option = new AutoGenerationOption();
      option.setIdentifierType(msfIdType);
      option.setSource(msfIdGenerator);
      option.setAutomaticGenerationEnabled(true);
      option.setManualEntryEnabled(true);
      Context.getService(IdentifierSourceService.class).saveAutoGenerationOption(option);
    }
  }

  /**
   * Treat this class as SequentialIdentifierGenerator which already has a
   * hibernate mapping/table
   */
  private SequentialIdentifierGenerator tranformType(MSFIdentifierGenerator msfIdentifierGenerator) {
    SequentialIdentifierGenerator identifierGen = new SequentialIdentifierGenerator();
    try {
      BeanUtils.copyProperties(identifierGen, msfIdentifierGenerator);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return identifierGen;
  }

  public static void installation() {
    new MSFIdentifierGenerator().install();
  }
}
