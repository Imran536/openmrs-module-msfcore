<%	
	ui.includeJavascript("coreapps", "fragments/patientdashboard/encountertemplate/defaultEncounterTemplate.js")
%>

<script type="text/template" id="defaultEncounterTemplate">
<li class="encounter" data-encounter-id="{{- encounter.encounterId }}" data-encounter-type-uuid="{{- encounter.encounterType.uuid }}" data-encounter-type-name="{{- encounter.encounterType.name }}" data-encounter-time="{{- encounter.encounterTime }}" data-encounter-date="{{- encounter.encounterDate }}" data-encounter-provider="{{- encounter.primaryProvider ? encounter.primaryProvider : '' }}" data-encounter-location="{{- encounter.location }}">
	<ul class="encounter-details">
	    <li> 
	        <div class="encounter-type">
	            <strong>
	                <span class="encounter-name" data-encounter-id="{{- encounter.encounterId }}">{{- encounter.form }}</span>
	            </strong>
	        </div>
	    </li>
	    <li>
	        <div class="details-action">
	            <a class="view-details collapsed" href='javascript:void(0);' data-encounter-id="{{- encounter.encounterId }}" data-encounter-form="{{- encounter.form != null}}" data-display-with-html-form="false" data-target="#encounter-summary{{- encounter.encounterId }}" data-toggle="collapse" data-target="#encounter-summary{{- encounter.encounterId }}">
	                <span class="show-details">${ ui.message("coreapps.patientDashBoard.showDetails")}</span>
	                <span class="hide-details">${ ui.message("coreapps.patientDashBoard.hideDetails")}</span>
	                <i class="icon-caret-right"></i>
	            </a>
	        </div>
	    </li>
	</ul>

	<span>
        {{ if ( (config.editable == null || config.editable) && encounter.canEdit) { }}
	        <i class="editEncounter edit-action icon-pencil" data-patient-id="{{- patient.id }}" data-encounter-id="{{- encounter.encounterId }}" {{ if (config.editUrl) { }} data-edit-url="{{- config.editUrl }}" {{ } }} title="${ ui.message("coreapps.edit") }"></i>
        {{ } }}
        {{ if ( encounter.canDelete ) { }}
	       <i class="deleteEncounterId delete-action icon-remove" data-visit-id="{{- encounter.visitId }}" data-encounter-id="{{- encounter.encounterId }}" title="${ ui.message("coreapps.delete") }"></i>
        {{  } }}
	</span>

	<div id="encounter-summary{{- encounter.encounterId }}" class="collapse">
	    <div class="encounter-summary-container"></div>
	</div>
</li>
</script>

<script type="text/template" id="defaultEncounterDetailsTemplate">
    {{ _.each(_.filter(diagnoses, function(d) { return d.answer }), function(d) { }}
        <p><small>{{- d.question}}</small><span>{{- d.answer}}</span></p>
    {{ }); }}

    {{ _.each(observations, function(observation) { }}
        {{ if(observation.answer != null) {}}
            <p><small>{{- observation.question}}</small><span>{{- observation.answer}}</span></p>
        {{}}}
    {{ }); }}

 <!--   {{ _.each(orders, function(order) { }}
        <p>
            <small>${ ui.message("coreapps.patientDashBoard.orderNumber")}</small><span>{{- order.orderNumber }}</span>
        </p>
        <p>
            <small>${ ui.message("coreapps.patientDashBoard.order")}</small><span>{{- order.concept }}</span>
        </p>
    {{ }); }} -->
</script>