<%
    ui.decorateWith("appui", "standardEmrPage", [ title: ui.message("msfcore.patientSummary") ])
%>

<script src="${ui.resourceLink('msfcore', 'scripts/msf.js')}"></script>
<link href="${ui.resourceLink('msfcore', 'styles/patientSummary.css')}" rel="stylesheet" type="text/css" media="all">

<script type="text/javascript">
    jQuery(function() {
    	jQuery("h2").text("${ui.message('msfcore.patientSummary')}"); 
    	// add demographics
	   	tabulateCleanedItemsIntoAnElement("#demograpics", [
	   		"${ui.message("msfcore.name")}${patientSummary.demographics.name}",
	   		"${ui.message("msfcore.age")}${patientSummary.demographics.age.age}",
	    	"${ui.message("msfcore.dob")}${patientSummary.demographics.age.formattedBirthDate}"
	    ]);
	    	
	    // add allergies
	    tabulateCleanedItemsIntoAnElement("#allergies", 
	    	"<% patientSummary.allergies.each { a -> %>|s|${ui.message('msfcore.allergy')}${a.name}|s|${ui.message('msfcore.reactions')}<% a.reactions.each { r -> %>${r}, <% } %>|s|${ui.message('msfcore.severity')}${a.severity}<% } %>"
	    		.split("|s|").filter(v=>v!=''),
	    3);
	    
	    var representation = "${patientSummary.representation}";
    	if(representation == 'SUMMARY') {
	    	// add clinicalNotes
		   	tabulateCleanedItemsIntoAnElement("#clinical-notes", 
		   		"<% patientSummary.clinicalNotes.each { n -> %>|s|${n.value}<% } %>".split("|s|").filter(v=>v!=''),
		    1);
		    
		    //add clinical history
	    	tabulateCleanedItemsIntoAnElement("#clinical-history", [
		    	"${ui.message("msfcore.patientSummary.medicalInfo")}<% patientSummary.clinicalHistory.medical.each { o -> %><% if(["false", "true", "Yes", "No"].contains(o.value) || o.value.isNumber()) { %>${o.name}: ${o.value}, <% } else { %>${o.value}, <% } %><% } %>",
		    	"${ui.message("msfcore.patientSummary.socialHistory")}<% patientSummary.clinicalHistory.social.each { o -> %><% if(["false", "true", "Yes", "No"].contains(o.value) || o.value.isNumber()) { %>${o.name}: ${o.value}, <% } else { %>${o.value}, <% } %><% } %>",
		    	"${ui.message("msfcore.patientSummary.familyHistory")}<% patientSummary.clinicalHistory.family.each { o -> %><% if(o.value != "_") { %>${o.value}, <% } %><% } %>",
		    	"${ui.message("msfcore.patientSummary.complications")}<% patientSummary.clinicalHistory.complications.each { o -> %>${o.value}, <% } %>",
		    	"${ui.message("msfcore.patientSummary.historyOfTargetOrganDamage")}<% patientSummary.clinicalHistory.targetOrganDamages.each { o -> %><% if(o.value != "_") { %>${o.value}, <% } %><% } %>",
		    	"${ui.message("msfcore.patientSummary.cardiovascularScore")}<% patientSummary.clinicalHistory.cardiovascularCholesterolScore.each { o -> %>${o.name}: ${o.value}, <% } %>",
		    	// "${ui.message("msfcore.patientSummary.blooodGlucose")}<% patientSummary.clinicalHistory.bloodGlucose.each { o -> %><% if(["false", "true", "Yes", "No"].contains(o.value) || o.value.isNumber()) { %>${o.name}: ${o.value}, <% } else { %>${o.value}, <% } %><% } %>",
		    	"${ui.message("msfcore.patientSummary.patientEducation")}<% patientSummary.clinicalHistory.patientEducation.each { o -> %><% if(["false", "true", "Yes", "No"].contains(o.value) || o.value.isNumber()) { %>${o.name}: ${o.value}, <% } else { %>${o.value}, <% } %><% } %>"
		    ], 1);
		    
	    	// add vitals
	    	tabulateCleanedItemsIntoAnElement("#vitals", [
	    		"${ui.message("msfcore.height")}${patientSummary.vitals.get(0).height.value} ${patientSummary.vitals.get(0).height.unit}",
	    		"${ui.message("msfcore.weight")}${patientSummary.vitals.get(0).weight.value} ${patientSummary.vitals.get(0).weight.unit}",
	    		"${ui.message("msfcore.bmi")}${patientSummary.vitals.get(0).bmi.value}",
	    		"${ui.message("msfcore.temperature")}${patientSummary.vitals.get(0).temperature.value} ${patientSummary.vitals.get(0).temperature.unit}",
	    		"${ui.message("msfcore.pulse")}${patientSummary.vitals.get(0).pulse.value} ${patientSummary.vitals.get(0).pulse.unit}",
	    		"${ui.message("msfcore.respiratoryRate")}${patientSummary.vitals.get(0).respiratoryRate.value} ${patientSummary.vitals.get(0).respiratoryRate.unit}",
	    		"${ui.message("msfcore.bloodPressure")}${patientSummary.vitals.get(0).bloodPressure.value}",
	    		"${ui.message("msfcore.bloodOxygenSaturation")}${patientSummary.vitals.get(0).bloodOxygenSaturation.value} ${patientSummary.vitals.get(0).bloodOxygenSaturation.unit}"
	    	], 3);
	    	
	    	// add diagnoses
	    	tabulateCleanedItemsIntoAnElement("#diagnoses", 
	    		"<% patientSummary.diagnoses.each { d -> %>|s|${d.name}<% } %>".split("|s|").filter(v=>v!=''),
	    	1);
	    	
	    	// add lab test results
	    	tabulateCleanedItemsIntoAnElement("#lab-tests", 
	    		"<% patientSummary.recentLabResults.each { m -> %>|s|${m.name} |s| ${m.value} |s| ${m.refRange} |s| ${m.encounterDate}<% } %>".split("|s|").filter(v=>v!=''),
	    	4);
	    	
	    	// add medications
	    	tabulateCleanedItemsIntoAnElement("#medications", 
	    		"<% patientSummary.currentMedications.each { m -> %>|s|${m.value}<% } %>".split("|s|").filter(v=>v!=''),
	    	1);
	    	
		    // add visits
	    	tabulateCleanedItemsIntoAnElement("#visits", 
	    		"<% patientSummary.visitDiagnosis.each { d -> %>|s|${d.visitDate}: ${d.name}<% } %>".split("|s|").filter(v=>v!=''),
	    	1);
	    		    	
    	} else if(representation == 'FULL') {
    		jQuery(document).prop('title', "${ui.message('msfcore.patientFullSummary')}");
    		jQuery("h2").text("${ui.message('msfcore.patientFullSummary')}");
    	
    		// TODO populate differing sections
    		
    		// followup clinical history - new
		    tabulateCleanedItemsIntoAnElementFupNote("#custom-div", [
		    	"<% patientSummary.clinicalHistoryListFup.each { n -> %>"+
		    	"${n.date}",
		    	"${ui.message("msfcore.patientSummary.medicalInfo")}<% n.medical.each { o -> %><% if(["false", "true", "Yes", "No"].contains(o.value) || o.value.isNumber()) { %>${o.name}: ${o.value}, <% } else { %>${o.value}, <% } %><% } %>",
		    	"${ui.message("msfcore.patientSummary.socialHistory")}<% n.social.each { o -> %><% if(["false", "true", "Yes", "No"].contains(o.value) || o.value.isNumber()) { %>${o.name}: ${o.value}, <% } else { %>${o.value}, <% } %><% } %>",
		    	"${ui.message("msfcore.patientSummary.familyHistory")}<% n.family.each { o -> %><% if(o.value != "_") { %>${o.value}, <% } %><% } %>",
		    	"${ui.message("msfcore.patientSummary.complications")}<% n.complications.each { o -> %>${o.value}, <% } %>",
		    	"${ui.message("msfcore.patientSummary.historyOfTargetOrganDamage")}<% n.targetOrganDamages.each { o -> %><% if(o.value != "_") { %>${o.value}, <% } %><% } %>",
		    	"${ui.message("msfcore.patientSummary.cardiovascularScore")}<% n.cardiovascularCholesterolScore.each { o -> %>${o.name}: ${o.value}, <% } %>",
		    	// "${ui.message("msfcore.patientSummary.blooodGlucose")}<% n.bloodGlucose.each { o -> %><% if(["false", "true", "Yes", "No"].contains(o.value) || o.value.isNumber()) { %>${o.name}: ${o.value}, <% } else { %>${o.value}, <% } %><% } %>",
		    	"${ui.message("msfcore.patientSummary.patientEducation")}<% n.patientEducation.each { o -> %><% if(["false", "true", "Yes", "No"].contains(o.value) || o.value.isNumber()) { %>${o.name}: ${o.value}, <% } else { %>${o.value}, <% } %><% } %>",
		    	"Clinical Note: ${n.clinicalNote}",
		    	+"<%}%>"
		    ], 1,9);
		    
		    // baseline clinical history 
		    jQuery("#baseline-header").html("Baseline note - ${patientSummary.clinicalHistory.date}");
		    tabulateCleanedItemsIntoAnElement("#baseline-clinical-history", [
		    	"${ui.message("msfcore.patientSummary.medicalInfo")}<% patientSummary.clinicalHistory.medical.each { o -> %><% if(["false", "true", "Yes", "No"].contains(o.value) || o.value.isNumber()) { %>${o.name}: ${o.value}, <% } else { %>${o.value}, <% } %><% } %>",
		    	"${ui.message("msfcore.patientSummary.socialHistory")}<% patientSummary.clinicalHistory.social.each { o -> %><% if(["false", "true", "Yes", "No"].contains(o.value) || o.value.isNumber()) { %>${o.name}: ${o.value}, <% } else { %>${o.value}, <% } %><% } %>",
		    	"${ui.message("msfcore.patientSummary.familyHistory")}<% patientSummary.clinicalHistory.family.each { o -> %><% if(o.value != "_") { %>${o.value}, <% } %><% } %>",
		    	"${ui.message("msfcore.patientSummary.complications")}<% patientSummary.clinicalHistory.complications.each { o -> %>${o.value}, <% } %>",
		    	"${ui.message("msfcore.patientSummary.historyOfTargetOrganDamage")}<% patientSummary.clinicalHistory.targetOrganDamages.each { o -> %><% if(o.value != "_") { %>${o.value}, <% } %><% } %>",
		    	"${ui.message("msfcore.patientSummary.cardiovascularScore")}<% patientSummary.clinicalHistory.cardiovascularCholesterolScore.each { o -> %>${o.name}: ${o.value}, <% } %>",
		    	// "${ui.message("msfcore.patientSummary.blooodGlucose")}<% patientSummary.clinicalHistory.bloodGlucose.each { o -> %><% if(["false", "true", "Yes", "No"].contains(o.value) || o.value.isNumber()) { %>${o.name}: ${o.value}, <% } else { %>${o.value}, <% } %><% } %>",
		    	"${ui.message("msfcore.patientSummary.patientEducation")}<% patientSummary.clinicalHistory.patientEducation.each { o -> %><% if(["false", "true", "Yes", "No"].contains(o.value) || o.value.isNumber()) { %>${o.name}: ${o.value}, <% } else { %>${o.value}, <% } %><% } %>",
		    	"Clinical Note: ${patientSummary.clinicalHistory.clinicalNote}"
		    ], 1);
		    
		    // add visits
	    	tabulateCleanedItemsIntoAnElement("#visits", 
	    		"<% patientSummary.visitDiagnosis.each { d -> %>|s|${d.visitDate}: ${d.name}<% } %>".split("|s|").filter(v=>v!=''),
	    	1);
	    	
	    	// add encounters
	    	tabulateCleanedItemsIntoAnElement("#encounters", 
	    		"<% patientSummary.encounters.each { d -> %>|s|${d.date}|s|${d.type}|s|${d.provider}<% } %>".split("|s|").filter(v=>v!=''),	    		
	    	3);
	    	
	    	// add historical vitals and obs
	    	tabulateCleanedItemsIntoAnElement("#historial-vitals", 
	    		"<% patientSummary.vitals.each { d -> %>|s|${d.dateCreated}|s|${ui.message("msfcore.height")}${d.height.value} ${d.height.unit}, ${ui.message("msfcore.weight")}${d.weight.value} ${d.weight.unit}, ${ui.message("msfcore.bmi")}${d.bmi.value}, ${ui.message("msfcore.temperature")}${d.temperature.value} ${d.temperature.unit}, ${ui.message("msfcore.pulse")}${d.pulse.value} ${d.pulse.unit}, ${ui.message("msfcore.respiratoryRate")}${d.respiratoryRate.value} ${d.respiratoryRate.unit}, ${ui.message("msfcore.bloodPressure")}${d.bloodPressure.value}, ${ui.message("msfcore.bloodOxygenSaturation")}${d.bloodOxygenSaturation.value} ${d.bloodOxygenSaturation.unit}<% } %>".split("|s|").filter(v=>v!='')
	    	, 2);
	    	
	    	// add diagnosis details
	    	tabulateCleanedItemsIntoAnElement("#diagnosis-details", 
	    		"<% patientSummary.visitDiagnosis.each { d -> %>|s|${d.name}|s|${d.status}|s|${d.visitDate}<% } %>".split("|s|").filter(v=>v!=''),
	    	3);
	    	
	    	// add medication details
	    	tabulateCleanedItemsIntoAnElement("#medication-details", 
	    		"<% patientSummary.medicationList.each { d -> %>|s|${d.name}|s|${d.frequency}|s|${d.quantity}|s|${d.duration}|s|${d.prescriptionDate}<% } %>".split("|s|").filter(v=>v!=''),
	    	5);
	    	
	    	// add appointment details
	    	tabulateCleanedItemsIntoAnElement("#appointment-details", 
	    		"<% patientSummary.appointmentList.each { d -> %>|s|${d.type}|s|${d.date}<% } %>".split("|s|").filter(v=>v!=''),
	    	2);
	    	
	    	// add demographics
		   	tabulateCleanedItemsIntoAnElement("#demograpics", [
		   		"${ui.message("msfcore.name")}${patientSummary.demographics.name}",
		   		"${ui.message("msfcore.age")}${patientSummary.demographics.age.age}",
		    	"${ui.message("msfcore.dob")}${patientSummary.demographics.age.formattedBirthDate}"
		    ]);
		    	
		    // add allergies
		    tabulateCleanedItemsIntoAnElement("#allergies", 
		    	"<% patientSummary.allergies.each { a -> %>|s|${ui.message('msfcore.allergy')}${a.name}|s|${ui.message('msfcore.reactions')}<% a.reactions.each { r -> %>${r}, <% } %>|s|${ui.message('msfcore.severity')}${a.severity}<% } %>"
		    		.split("|s|").filter(v=>v!=''),
		    3);
		    // add lab test results
	    	tabulateCleanedItemsIntoAnElement("#lab-tests", 
	    		"<% patientSummary.recentLabResults.each { m -> %>|s|${m.name} |s| ${m.value} |s| ${m.refRange} |s| ${m.encounterDate}<% } %>".split("|s|").filter(v=>v!=''),
	    	4);
    	}
    	
    	jQuery("#print-patient-summary").click(function(e) {
    		printPageWithIgnore(".summary-actions-wrapper");
    	});
    });
</script>
<div id="patient-summary">
	<div id="patient-summary-header">   
		<div class="logo" class="left">
			<img src="${ui.resourceLink("msfcore", "images/msf_logo.png")}"  height="50" width="100"/>
		</div>
		<div class="right">
			${patientSummary.facility}
		</div>
	</div>
	
	<h2/>
	
	<h4 >${ui.message("msfcore.patientSummary.demograpicDetails")}</h4>
	<div id="demograpics"></div>
	
	<h4>${ui.message("msfcore.patientSummary.workingDiagnosis")}</h4>
	<div id="diagnoses"></div>
	
	<h4>${ui.message("msfcore.patientSummary.knownAllergies")}</h4>
	<div id="allergies"></div>
	
	<h4>${ui.message("msfcore.patientSummary.recentLabTest")}</h4>
	<div id="lab-tests"></div>
		
	<h4>${ui.message("msfcore.ncdfollowup.visitdetails.title")}</h4>
	<div id="visits"></div>
	
	<h4>${ui.message("msfcore.encounterDetails")}</h4>
	<div id="encounters"></div>
	
	<h4>Historical vitals and Obs</h4>
	<div id="historial-vitals"></div>
	
	<h4>Diagnosis Details</h4>
	<div id="diagnosis-details"></div>
	
	<h4 id="baseline-header">Baseline note</h4>
	<div id="baseline-clinical-history"></div>
	
	<h4>Medication Details</h4>
	<div id="medication-details"></div>
	
	<h4>Planned Appointments</h4>
	<div id="appointment-details"></div>
	
	<div id="custom-div"></div>
	
	<div id="patient-summary-signature">
		<div class="left">
			<b>${ui.message("msfcore.patientSummary.provider")}</b>${patientSummary.provider}
		</div>
		<div class="right">
			<b>${ui.message("msfcore.patientSummary.signature")}</b>
		</div>
	</div>
</div>

<div class="summary-actions-wrapper">
	<div class="summary-actions">
		<input id="print-patient-summary" type="button" value="${ ui.message('msfcore.print')}"/>
		<input type="button" onclick="history.back();" value="${ ui.message('msfcore.close')}"/>
	</div>
</div>
