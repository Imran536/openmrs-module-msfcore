<%
    ui.decorateWith("appui", "standardEmrPage", [ title: ui.message("msfcore.patientSummary") ])
%>

<script src="${ui.resourceLink('msfcore', 'scripts/msf.utils.js')}"></script>
<link href="${ui.resourceLink('msfcore', 'styles/patientSummary.css')}" rel="stylesheet" type="text/css" media="all">

<script type="text/javascript">
    jQuery(function() {
	    var representation = "${patientSummary.representation}";
    	if(representation == 'FULL') {
    		jQuery(document).prop('title', "${ui.message('msfcore.patientFullRecord.title')}");
    		jQuery("h2").text("${ui.message('msfcore.patientFullRecord.title')}");
    	
    		// TODO populate differing sections
    		
    		// followup clinical history
		    tabulateCleanedItemsIntoAnElementFupNote("#followup-note-div", [
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
		    ],9);
		    
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
		    
    	    // add visits-summary
			tabulateCleanedItemsIntoAnElement("#visits-summary", 
				"1st Visit: ${patientSummary.visitSummary.firstVisitDate} |s|Last Visit: ${patientSummary.visitSummary.lastVisitDate}|s|Total Visits: ${patientSummary.visitSummary.totalVisits}|s|Last Seen By: ${patientSummary.visitSummary.lastSeenBy}".split("|s|").filter(v=>v!=''),
			4);
		    
		    
		    // add visits
	    	tabulateCleanedItemsIntoAnElementWithHeader("#visits", 
	    		"<% patientSummary.visitDiagnosis.each { d -> %>|s|${d.visitDate}|s|${d.name}<% } %>".split("|s|").filter(v=>v!=''),
	    	2);
	    	
	    	// add encounters
	    	tabulateCleanedItemsIntoAnElementWithHeader("#encounters", 
	    		"<% patientSummary.encounters.each { d -> %>|s|${d.date}|s|${d.type}|s|${d.provider}<% } %>".split("|s|").filter(v=>v!=''),	    		
	    	3);
	    	
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
	    	
	    	// add historical vitals and obs
	    	tabulateCleanedItemsIntoAnElementWithHeader("#historial-vitals", 
	    		"<% patientSummary.vitals.each { d -> %>|s|${d.dateCreated}|s|${ui.message("msfcore.height")}${d.height.value} ${d.height.unit}, ${ui.message("msfcore.weight")}${d.weight.value} ${d.weight.unit}, ${ui.message("msfcore.bmi")}${d.bmi.value}, ${ui.message("msfcore.temperature")}${d.temperature.value} ${d.temperature.unit}, ${ui.message("msfcore.pulse")}${d.pulse.value} ${d.pulse.unit}, ${ui.message("msfcore.respiratoryRate")}${d.respiratoryRate.value} ${d.respiratoryRate.unit}, ${ui.message("msfcore.bloodPressure")}${d.bloodPressure.value}, ${ui.message("msfcore.bloodOxygenSaturation")}${d.bloodOxygenSaturation.value} ${d.bloodOxygenSaturation.unit}<% } %>".split("|s|").filter(v=>v!='')
	    	, 2);
	    	    	
			// add medication details
	    	tabulateCleanedItemsIntoAnElementWithHeader("#medication-details", 
	    		"<% patientSummary.medicationList.each { d -> %>|s|${d.name}|s|${d.frequency}|s|${d.quantity}|s|${d.duration}|s|${d.dispensed}|s|${d.prescriptionDate}|s|${d.status}<% } %>".split("|s|").filter(v=>v!=''),
	    	7);
	    	
	    	// add referrals
	    	tabulateCleanedItemsIntoAnElementWithHeader("#referrals", 
	    		"<% patientSummary.patientReferrals.each { m -> %>|s|${m.referredTo}|s|${m.referralDate}|s|${m.feedback}|s|${m.provider}<% } %>".split("|s|").filter(v=>v!=''),
	    	4);
	    	
	    	// add appointment details
	    	tabulateCleanedItemsIntoAnElementWithHeader("#appointment-details",
	    		"<% patientSummary.appointmentList.each { d -> %>|s|${d.type}|s|${d.date}<% } %>".split("|s|").filter(v=>v!=''),
	    	2);
	    	
	    	// add demographics
		   	tabulateCleanedItemsIntoAnElement("#demograpics", [
		   		"${ui.message("msfcore.name")}${patientSummary.demographics.name}",
		   		"${ui.message("msfcore.age")}${patientSummary.demographics.age.age}",
		    	"${ui.message("msfcore.dob")}${patientSummary.demographics.age.formattedBirthDate}",
		    	"${ui.message("msfcore.gender")}${patientSummary.demographics.gender}"
		    ]);
		    
	    	// add working diagnoses
	    	tabulateCleanedItemsIntoAnElement("#diagnoses", 
	    		"<% patientSummary.diagnoses.each { d -> %>|s|${d.label} ${d.name}|s|Date recorded: ${d.dateRecorded}<% } %>".split("|s|").filter(v=>v!=''),
	    	2);
		    
		    // add allergies
		    tabulateCleanedItemsIntoAnElement("#allergies", 
		    	"<% patientSummary.allergies.each { a -> %>|s|${ui.message('msfcore.allergy')}${a.name}|s|${ui.message('msfcore.reactions')}<% a.reactions.each { r -> %>${r}, <% } %>|s|${ui.message('msfcore.severity')}${a.severity}<% } %>"
		    		.split("|s|").filter(v=>v!=''),
		    3);
		    
		    // add lab test results
	    	tabulateCleanedItemsIntoAnElementWithHeader("#lab-tests", 
	    		"<% patientSummary.recentLabResults.each { m -> %>|s|${m.name} |s| ${m.value} |s|${m.unit} |s| ${m.refRange} |s| ${m.encounterDate}<% } %>".split("|s|").filter(v=>v!=''),
	    	5);
    	}    	
    	jQuery("#print-patient-summary").click(function(e) {
    		printPageWithIgnoreInclude(".summary-actions-wrapper");
    	});
    });
</script>
<div id="patient-full-record">
	<div id="patient-full-record-header">   
		<div class="logo" class="left">
			<img src="${ui.resourceLink("msfcore", "images/msf_logo.png")}"  height="50" width="100"/>
		</div>
		<div class="right">
			<div>${patientSummary.address.address1}</div>
			<div>${patientSummary.address.address2}</div>
			<div>${patientSummary.address.city}</div>
			<div>${patientSummary.address.country}</div>			
			<br/>
			<div>${patientSummary.reportDate}</div>
		</div>
	</div>
	
	<h2></h2>
	
	<div>
		<div class="left">Patient ID: ${patientSummary.patientIdentifier}</div>
		<div class="right">${patientSummary.programName}</div>
	</div>
	
	<br><br>
		
	<h4 >${ui.message("msfcore.patientSummary.demograpicDetails")}</h4>
	<div id="demograpics"></div>
	
	<h4>${ui.message("msfcore.ncdfollowup.visitdetails.title")}</h4>
	<div id="visits-summary"></div>
	
	<h4>${ui.message("msfcore.patientSummary.recentVitalsAndObservations")} - ${patientSummary.vitals.get(0).dateCreated}</h4>
	<div id="vitals"></div>
	
	<h4>${ui.message("msfcore.ncdfollowup.visitdetails.title")}</h4>
	<div id="visits">
		<table id="visits">
			<tr>
				<th>Visit Date</th>
				<th>Diagnosis</th>
			</tr>
		</table>
	</div>
	
	<h4>${ui.message("msfcore.patientSummary.workingDiagnosis")}</h4>
	<div id="diagnoses"></div>
	
	<h4>${ui.message("msfcore.patientSummary.knownAllergies")}</h4>
	<div id="allergies"></div>
	
	<h4>${ui.message("msfcore.encounterDetails.header")}</h4>
	<div id="encounters">
		<table id="encounters">
			<tr>
				<th>Encounter Date</th>
				<th>Encounter Type</th>
				<th>Provider</th>
			</tr>
		</table>
	</div>
	
	<h4>${ui.message("msfcore.historicalVitals.header")}</h4>
	<div id="historial-vitals">
		<table id="historial-vitals">
			<tr>
				<th>Visit Date</th>
				<th>Vitals</th>
			</tr>
		</table>
	</div>
	
	<h4 id="baseline-header">${ui.message("msfcore.baselineNote.header")}</h4>
	<div id="baseline-clinical-history"></div>
	
	<div id="followup-note-div"></div>
	
	<h4>${ui.message("msfcore.patientSummary.recentLabTest")}</h4>
	<div>
		<table id="lab-tests">
			<tr>
				<th>Test</th>
				<th>Result</th>
				<th>Unit</th>
				<th>Reference Range</th>
				<th>Encounter Date</th>
			</tr>
		</table>
	</div>
	
	<h4>${ui.message("msfcore.patientSummary.currentMedication")}</h4>
	<div>
		<table id="medication-details">
			<tr>
				<th>Drug</th>
				<th>Frequency</th>
				<th>Quantity</th>
				<th>Duration</th>
				<th>Dispensed</th>
				<th>Prescription Date</th>
				<th>Status</th>
			</tr>
		</table>
	</div>
	
	<h4>Referrals</h4>
	<div>
		<table id="referrals">
			<tr>
				<th>Referred To</th>
				<th>Referreal Date</th>
				<th>Feedback</th>
				<th>Provider</th>				
			</tr>
		</table>
	</div>
	
	<h4>${ui.message("msfcore.plannedAppointments.header")}</h4>
	<div id="appointment-details">
		<table id="appointment-details">
			<tr>
				<th>Appointment Type</th>
				<th>Appointment Date</th>
			</tr>
		</table>
	</div>
	
	<h4>${ui.message("msfcore.diagnosisDetails.header")}</h4>
	
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
