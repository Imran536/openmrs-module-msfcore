<script src="${ui.resourceLink('msfcore', 'scripts/msf.utils.js')}"></script>
<script src="${ui.resourceLink('msfcore', 'scripts/angular.pagination.js')}"></script>
<script src="${ui.resourceLink('msfcore', 'scripts/results.controller.js')}"></script>
<link href="${ui.resourceLink('msfcore', 'styles/results.css')}" rel="stylesheet" type="text/css" media="all">

<script type="text/javascript">
    jQuery(function() {
    	jQuery("#print-results").click(function(e) {
    		printPageWithIgnoreInclude(".print-ignore", ".print-include");
    	});
	});
</script>

<div id="results-header" class="print-ignore">
	<h3 class="pull-left">${pageLabel}</h3>
	<input type="button" class="pull-right" id="print-results" value="${ui.message('msfcore.printResults')}"/>
</div>
<br />
<div ng-app="resultsApp" ng-controller="ResultsController" ng-init="retrieveResults(true)">
	<div class="print-ignore" ng-if="results.filters">
		<table id="filters-table">
			<tbody>
				<tr>
					<td ng-if="results.filters.name">{{results.filters.name}} ${ui.message('general.search')}</td>
					<td ng-if="results.filters.statuses">${ui.message('msfcore.statusFilter')}</td>
					<td ng-if="results.filters.providers">${ui.message('msfcore.providerFilter')}</td>
					<td ng-if="results.filters.dates">${ui.message('msfcore.dateFilter')}</td>
				</tr>
				<tr>
					<td ng-if="results.filters.name" class="no-wrap">
						<input id="filter-name"/><input type="button" ng-click="nameFilter()" value="${ui.message('general.search')}"/>
					</td>
					<td ng-if="results.filters.statuses" class="no-wrap">
						<select ng-model="filterStatusValue" id="filter-status" ng-change="statusFilter()">
							<option value="all" ng-if="results.resultCategory == 'LAB_RESULTS'">${ui.message('msfcore.statusAll')}</option>
							<option value="all" ng-if="results.resultCategory == 'DRUG_LIST' || results.resultCategory == 'REFERRAL_LIST'">${ui.message('msfcore.statusAll')}</option>
							<option ng-repeat="status in results.filters.statuses" value="{{status}}">{{status.charAt(0).toUpperCase() + status.substr(1).toLowerCase();}}</option>
						</select>
					</td>
					<td ng-if="results.filters.providers" class="no-wrap">
						<select ng-model="filterProviderValue" id="filter-provider" ng-change="providerFilter()">
							<option value="all">${ui.message('msfcore.providerAll')}</option>
							<option ng-repeat="provider in results.filters.providers" value="{{provider.name}}">{{provider.name}}</option>
						</select>
					</td>
					<td ng-if="results.filters.dates" class="no-wrap">
						<div>
							<input onfocus="this.type='date'" id="filter-start-date" placeholder="${ui.message('msfcore.startDate')}" ng-model="filterStartDate" ng-change="datesFilter()" />
							<input onfocus="this.type='date'" id="filter-end-date" placeholder="${ui.message('msfcore.endDate')}" ng-model="filterEndDate" ng-change="datesFilter()" />
							<select id="filter-dates" ng-model="filterDateValue" ng-change="datesFilter()"">
								<option ng-repeat="date in results.filters.dates" value="{{date}}">{{date}}</option>
							</select>
							<input type="button" onclick="retrieveResults()" value="${ui.message('msfcore.clear')}"/>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	</br>
	<div id="results-data">
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
			    		<span ng-if="result.actions.value.includes('EDIT') > 0"><i class="icon-edit" ng-click="edit(\$event, result);"></i></span>
			    		<span ng-if="result.actions.value.includes('DELETE') > 0"><i class="icon-trash" ng-click="purge(result);"></i></span>
			    	</td>
			    	<td ng-if="!result.actions.value || result.actions.value.length == 0" class="print-ignore"></td>
		    </tbody>
		</table>
	</div>
	<div class="print-ignore wrap-center pagination">
		<div class="left">
			<span><input type="button" onclick="history.back();" value="${ ui.message('general.back')}"/></span>
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
		<div class="right showing-pages" ng-init="scp = this">
			<span class='page' ng-repeat="page in pages" ng-class="{'current-page':page.page==currentPage}" ng-click="paginate(scp, page, retrieveResults)"> {{page.page}} </span>
			<span ng-class="{'page':nextPage, 'disabled': !nextPage}" ng-click="paginate(scp, nextPage, retrieveResults)">${ui.message('general.next')}</span>
			<span ng-class="{'page':previousPage, 'disabled': !previousPage}" ng-click="paginate(scp, previousPage, retrieveResults)">${ui.message('general.previous')}</span>
		</div>
		<div class="center pages">
			<span class="disabled">
				${ui.message('msfcore.showing')} {{results.pagination.fromItemNumber}} ${ui.message('general.to')} {{results.pagination.toItemNumber}} ${ui.message('general.of')} {{results.pagination.totalItemsNumber}} ${ui.message('msfcore.entries').toLowerCase()}
			</span>	
		</div>
	</div>
</div>