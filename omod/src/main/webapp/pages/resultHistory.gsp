<%
    ui.decorateWith("appui", "standardEmrPage", [ title: ui.message('msfcore.resultList') ])
%>

<script type="text/javascript">
    var breadcrumbs = [
        { icon: "icon-home", link: '/' + OPENMRS_CONTEXT_PATH + '/index.htm' },
        { label: "${ ui.encodeHtmlContent(ui.format(patient.person.personName.familyName)) } ${ ui.encodeHtmlContent(ui.format(patient.person.personName.givenName)) }",
            link: "${ ui.pageLink("coreapps", "clinicianfacing/patient", [patientId: patient.uuid]) }" },
        { label: "${ ui.message('msfcore.resultList') }" }
    ];
</script>

<div class="print-include wrap-center">
	<div class="logo" class="center">
		<img src="${ui.resourceLink("msfcore", "images/msf_logo.png")}"  height="150" width="300"/>
	</div>
</div>

${ ui.includeFragment("coreapps", "patientHeader", [ patient: patient ]) }

${ ui.includeFragment("msfcore", "results") }
