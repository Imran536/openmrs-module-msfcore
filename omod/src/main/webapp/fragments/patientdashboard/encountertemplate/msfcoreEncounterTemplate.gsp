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

<script type="text/javascript">

	function mergeWithPreviousItem(array, index) {
		array[index-1] = array[index-1] + ',' + array[index]
		array.splice(index, 1)
	}

	function mergeElementsWithNoColon(array) {
		var i = 1
		while(i < array.length - 1) {
			if (!array[i].includes(':')) {
				mergeWithPreviousItem(array, i)
			}
			i++
		}
		return array
	}

	function reorderContent(content) {
		var values = content.split(',')

		values.forEach (function (value, index) {
			values[index] = value.trim()
		})

		mergeElementsWithNoColon(values)

		var sortedValues = values.sort()
		return sortedValues.join()
	}

	function replaceAllValues(content, searchValue, newValue) {
		var currentContent = content;
		var changed;
		do {
			var newContent = currentContent.replace(searchValue, newValue);
			changed = newContent !== currentContent;
			currentContent = newContent;
		} while (changed);
		return reorderContent(currentContent);
	}

	function removeObservationRedundancy(o) {
		o.answer = o.answer.toUpperCase();
		o.question = o.question.toUpperCase();
		var cleanAnswer = replaceAllValues(o.answer, o.question + ': ', '');
		o.answer = cleanAnswer;
	}

</script>

<script type="text/template" id="defaultEncounterDetailsTemplate">

    {{ _.each(_.filter(diagnoses, function(d) { return d.answer }), function(d) { }}
        <p><small>{{- d.question}}</small><span>{{- d.answer}}</span></p>
    {{ }); }}

    {{ _.each(observations, function(observation) { }}
		{{ removeObservationRedundancy(observation); }}
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