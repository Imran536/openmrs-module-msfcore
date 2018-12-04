<%
    ui.decorateWith("appui", "standardEmrPage", [ title: ui.message("msfcore.patientSummary") ])
%>

<script src="${ui.resourceLink('msfcore', 'scripts/msf.utils.js')}"></script>
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
	    
	    // add clinical history
	    tabulateCleanedItemsIntoAnElement("#clinical-history", [
	    	"${ui.message("msfcore.patientSummary.medicalInfo")}<% patientSummary.clinicalHistory.medical.each { o -> %><% if(["false", "true", "Yes", "No"].contains(o.value) || o.value.isNumber()) { %>${o.name}: ${o.value}, <% } else { %>${o.value}, <% } %><% } %>",
	    	"${ui.message("msfcore.patientSummary.socialHistory")}<% patientSummary.clinicalHistory.social.each { o -> %><% if(["false", "true", "Yes", "No"].contains(o.value) || o.value.isNumber()) { %>${o.name}: ${o.value}, <% } else { %>${o.value}, <% } %><% } %>",
	    	"${ui.message("msfcore.patientSummary.familyHistory")}<% patientSummary.clinicalHistory.family.each { o -> %><% if(o.value != "_") { %>${o.value}, <% } %><% } %>",
	    	"${ui.message("msfcore.patientSummary.complications")}<% patientSummary.clinicalHistory.complications.each { o -> %>${o.value}, <% } %>",
	    	"${ui.message("msfcore.patientSummary.historyOfTargetOrganDamage")}<% patientSummary.clinicalHistory.targetOrganDamages.each { o -> %><% if(["false", "true", "Yes", "No"].contains(o.value) || o.value.isNumber()) { %>${o.name}: ${o.value}, <% } else { %>${o.value}, <% } %><% } %>",
	    	"${ui.message("msfcore.patientSummary.cardiovascularScore")}<% patientSummary.clinicalHistory.cardiovascularCholesterolScore.each { o -> %>${o.name}: ${o.value}, <% } %>",
	    	// "${ui.message("msfcore.patientSummary.blooodGlucose")}<% patientSummary.clinicalHistory.bloodGlucose.each { o -> %><% if(["false", "true", "Yes", "No"].contains(o.value) || o.value.isNumber()) { %>${o.name}: ${o.value}, <% } else { %>${o.value}, <% } %><% } %>",
	    	"${ui.message("msfcore.patientSummary.patientEducation")}<% patientSummary.clinicalHistory.patientEducation.each { o -> %><% if(["false", "true", "Yes", "No"].contains(o.value) || o.value.isNumber()) { %>${o.name}: ${o.value}, <% } else { %>${o.value}, <% } %><% } %>"
	    ], 1);
	    	
	   	// add clinicalNotes
	   	tabulateCleanedItemsIntoAnElement("#clinical-notes", 
	   		"<% patientSummary.clinicalNotes.each { n -> %>|s|${n.value}<% } %>".split("|s|").filter(v=>v!=''),
	    1);
	    
	    var representation = "${patientSummary.representation}";
    	if(representation == 'SUMMARY') {
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
	    		"<% patientSummary.recentLabResults.each { m -> %>|s|${m.name}: ${m.value}<% } %>".split("|s|").filter(v=>v!=''),
	    	2);
	    	
	    	// add medications
	    	tabulateCleanedItemsIntoAnElement("#medications", 
	    		"<% patientSummary.currentMedications.each { m -> %>|s|${m.value}<% } %>".split("|s|").filter(v=>v!=''),
	    	1);
    	} else if(representation == 'FULL') {
    		jQuery(document).prop('title', "${ui.message('msfcore.patientFullSummary')}");
    		jQuery("h2").text("${ui.message('msfcore.patientFullSummary')}");
    	
    		// TODO populate differing sections
    	}
    	
    	jQuery("#print-patient-summary").click(function(e) {
    		printPageWithIgnoreInclude(".summary-actions-wrapper");
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
	
	<h4>${ui.message("msfcore.patientSummary.recentVitalsAndObservations")}</h4>
	<div id="vitals"></div>
	
	<h4>${ui.message("msfcore.patientSummary.workingDiagnosis")}</h4>
	<div id="diagnoses"></div>
	
	<h4>${ui.message("msfcore.patientSummary.knownAllergies")}</h4>
	<div id="allergies"></div>
	
	<h4>${ui.message("msfcore.patientSummary.clinicalHistory")}</h4>
	<div id="clinical-history"></div>
	
	<h4>${ui.message("msfcore.patientSummary.recentLabTest")}</h4>
	<div id="lab-tests"></div>
	
	<h4>${ui.message("msfcore.patientSummary.currentMedication")}</h4>
	<div id="medications"></div>
	
	<h4>${ui.message("msfcore.patientSummary.clinicalNotes")}</h4>
	<div id="clinical-notes"></div>
	
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
