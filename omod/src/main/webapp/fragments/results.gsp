<script src="${ui.resourceLink('msfcore', 'scripts/msf.utils.js')}"></script>
<script src="${ui.resourceLink('msfcore', 'scripts/angular.pagination.js')}"></script>
<script src="${ui.resourceLink('msfcore', 'scripts/results.controller.js')}"></script>

<script type="text/javascript">
    jQuery(function() {
    	jQuery("#print-results").click(function(e) {
    		printPageWithIgnoreInclude(".print-ignore", ".print-include");
    	});
	});
</script>

<div class="content-box flex-col">

	<div id="results-header" class="print-ignore margin-b-16 flex-row flex-space-btwn">
		<h3 class="pull-left">${pageLabel}</h3>
		<input type="button" class="pull-right" id="print-results" value="${ui.message('msfcore.printResults')}"/>
	</div>

	<div ng-app="resultsApp" ng-controller="ResultsController" ng-init="retrieveResults(true)">

		<div class="flex-row margin-y-0 pad-b-16 bg-teal-light top-rounded drop-shadow-mid print-ignore" ng-if="results.filters">

			<div id="filter-name" ng-if="results.filters.name" class="flex-row pad-16 margin-0">
				<span class="flex-col margin-r-8">
					<label>{{results.filters.name}} ${ui.message('general.search')}</label>
					<input id="filter-name"/>
				</span>
				<button class="margin-t-20 margin-b-0" ng-click="nameFilter()">${ui.message('general.search')}</button>
			</div>
 
			<div id="filter-status" ng-if="results.filters.statuses" class="flex-col pad-16 margin-0">
				<label>${ui.message('msfcore.statusFilter')}</label>
				<select ng-model="filterStatusValue" id="filter-status" ng-change="statusFilter()">
					<option value="all" ng-if="results.resultCategory == 'LAB_RESULTS'">${ui.message('msfcore.statusAll')}</option>
					<option value="all" ng-if="results.resultCategory == 'DRUG_LIST' || results.resultCategory == 'REFERRAL_LIST'">${ui.message('msfcore.statusAll')}</option>
					<option ng-repeat="status in results.filters.statuses" value="{{status}}">{{status.charAt(0).toUpperCase() + status.substr(1).toLowerCase();}}</option>
				</select>
			</div>

			<div ng-if="results.filters.providers" class="flex-col pad-16 margin-0">
				<label>${ui.message('msfcore.providerFilter')}</label>
				<select ng-model="filterProviderValue" id="filter-provider" ng-change="providerFilter()">
					<option value="all">${ui.message('msfcore.providerAll')}</option>
					<option ng-repeat="provider in results.filters.providers" value="{{provider.name}}">{{provider.name}}</option>
				</select>
			</div>

			<div id="filter-date" ng-if="results.filters.dates" class="flex-row pad-16 margin-0">
				<div class="flex-col margin-r-8">
					<label>${ui.message('msfcore.dateFilter')}</label>
					<select id="filter-dates" ng-model="filterDateValue" ng-change="datesFilter()"">
						<option ng-repeat="date in results.filters.dates" value="{{date}}">{{date}}</option>
					</select>
				</div>
				<div class="flex-col margin-r-8">
					<label>${ui.message('msfcore.startDate')}</label>
					<input type="date" id="filter-start-date" ng-model="filterStartDate" ng-change="datesFilter()" />
				</div>
				<div class="flex-col margin-r-8">
					<label>${ui.message('msfcore.endDate')}</label>
					<input type="date" id="filter-end-date" ng-model="filterEndDate" ng-change="datesFilter()" />
				</div>
				<button class="margin-t-20 margin-b-0" onclick="retrieveResults()">${ui.message('msfcore.clear')}</button>
			</div>
		</div>

		<div id="results-data" class="drop-shadow-mid">
			<table>
				<thead>
					<tr>
						<th ng-repeat="key in results.keys">{{key}}</th>
						<th class="print-ignore">${ui.message('msfcore.actions')}</th>
					</tr>
				</thead>
				<tbody>
					<tr ng-repeat="result in results.results" id="{{result.uuid.value}}">
						<td ng-repeat="key in results.keys">
							<ng-bind-html ng-bind-html="renderResultValue(result, key)"></ng-bind-html>
						</td>
						<td ng-if="result.actions.value.length > 0" class="print-ignore">
							<span class="btn-small" ng-if="result.actions.value.includes('EDIT') > 0" ng-click="edit(\$event, result);">
								<i class="icon-edit"></i> Edit
							</span>
							<span class="btn-small" ng-if="result.actions.value.includes('DELETE') > 0" ng-click="purge(result);">
								<i class="icon-trash"></i> Delete
							</span>
						</td>
						<td ng-if="!result.actions.value || result.actions.value.length == 0" class="print-ignore"></td>
				</tbody>
			</table>
		</div>

		<div class="print-ignore text-center pagination flex-row margin-y-0 bottom-rounded drop-shadow-mid">
			<div class="pad-x-24 pad-y-16">
				<!--<button onclick="history.back();">${ ui.message('general.back')}"</button>-->
				<span>${ui.message('msfcore.show')}</span>
				<span>
					<select ng-model="resultsPerPage" ng-change="pagination(this, retrieveResultsInitialisePages)">
						<option value="25">25</option>
						<option value="50" ng-show="results.pagination.totalItemsNumber > 25">50</option>
						<option value="100" ng-show="results.pagination.totalItemsNumber > 50">100</option>
						<option value="all" ng-show="results.pagination.totalItemsNumber > 100">${ui.message('msfcore.all')}</option>
					</select>
				</span>
				<span>${ui.message('msfcore.entries')}</span>
			</div>
			<div class="pad-x-24 pad-t-24">
				<span class="disabled">
					${ui.message('msfcore.showing')} {{results.pagination.fromItemNumber}} ${ui.message('general.to')} {{results.pagination.toItemNumber}} ${ui.message('general.of')} {{results.pagination.totalItemsNumber}} ${ui.message('msfcore.entries').toLowerCase()}
				</span>	
			</div>
			<div class="pad-x-24 pad-t-24" ng-init="scp = this">
				<span class='page' ng-repeat="page in pages" ng-class="{'current-page':page.page==currentPage}" ng-click="paginate(scp, page, retrieveResults)"> {{page.page}} </span>
				<span ng-class="{'page':nextPage, 'disabled': !nextPage}" ng-click="paginate(scp, nextPage, retrieveResults)">${ui.message('general.next')}</span>
				<span ng-class="{'page':previousPage, 'disabled': !previousPage}" ng-click="paginate(scp, previousPage, retrieveResults)">${ui.message('general.previous')}</span>
			</div>
		</div>

	</div>

</div>		
