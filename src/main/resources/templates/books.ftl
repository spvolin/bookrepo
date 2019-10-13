
<#include "*/navbar.ftl"> 

<!--    ----------------------------------    BookRepo3: Books    ----------------------------------   -->

<div class="generic-container">
	<div class="panel panel-default">
		<!-- Default panel contents -->
		<div class="panel-heading">
			<span class="lead">Book: </span>
		</div>
		<div class="panel-body">
			<div class="formcontainer">
				<div class="alert alert-success" role="alert" ng-if="ctrlBook.successMessage">{{ctrlBook.successMessage}}</div>
				<div class="alert alert-danger" role="alert" ng-if="ctrlBook.errorMessage">{{ctrlBook.errorMessage}}</div>
				<form ng-submit="ctrlBook.submit()" name="myForm" class="form-horizontal">
					<input type="hidden" ng-model="ctrlBook.book.id" />
					<div class="row">
						<div class="form-group col-md-12">
							<label class="col-md-2 control-lable" for="bname">Author name:</label>
							<div class="col-md-7">
								<input type="text" ng-model="ctrlBook.book.authorName" id="bname" class="authorname form-control input-sm" placeholder="Enter author name" required ng-minlength="3" />
							</div>
						</div>
					</div>

					<div class="row">
						<div class="form-group col-md-12">
							<label class="col-md-2 control-lable" for="bookTitle">Book Title:</label>
							<div class="col-md-7">
								<input type="text" ng-model="ctrlBook.book.bookTitle" id="bookTitle" class="form-control input-sm" placeholder="Enter book title" required ng-minlength="3" />
							</div>
						</div>
					</div>

					<div class="row">
						<div class="form-group col-md-12">
							<label class="col-md-2 control-lable" for="isbn">ISBN</label>
							<div class="col-md-7">
								<input type="text" ng-model="ctrlBook.book.isbn" id="isbn" class="form-control input-sm" placeholder="Enter ISBN" required required ng-minlength="3" />
							</div>
						</div>
					</div>

					<div class="row">
						<div class="form-group col-md-12">
							<label class="col-md-2 control-lable" for="files">Files:</label>
							<div class="col-md-7">
								<input type="file" name="file" ng-model="ctrlBook.book.files" id="file_upload" class="form-control" onchange="angular.element(this).scope().filesSelected(this)" upload-files multiple/>
								<ul>
									<li id="file-li" ng-repeat="file in files">{{file.name}}</li>
								</ul>
							</div>
						</div>
					</div>

					<div class="row">
						<div class="form-actions floatRight">
							<input type="submit" value="{{!ctrlBook.book.id ? 'Add' : 'Update'}}" class="btn btn-primary btn-sm" ng-disabled="myForm.$invalid || (myForm.$pristine && !inputContainsFile)  ">
							<button type="button" ng-click="ctrlBook.reset()" class="btn btn-warning btn-sm" ng-disabled="myForm.$pristine">Reset Form</button>
						</div>
					</div>
				</form>
			</div>
            <div class="upload-response">
           		<div id="multipleFileUploadError"></div>
               	<div id="multipleFileUploadSuccess"></div>
			</div>			
		</div>
	</div>
	<div class="panel panel-default">
		<!-- Default panel contents -->
		<div class="panel-heading">
			<span class="lead">List of Books </span>
		</div>
		<div class="panel-body">

			<div class="row" style="margin-bottom: 20px;">
				<div class="col-md-12">
					<label class="col-md-1 lable label-style" for="search">Search:</label>
					<div class="col-md-6">
						<input type="text" id="search" class="form-control input-sm" ng-model="ctrlBook.searchedString" ng-change="ctrlBook.searchBooks=true" placeholder="..."></input>
					</div>
					<div class="col-md-5">
						<select ng-model="ctrlBook.searchedCriterion" ng-change="ctrlBook.searchBooks=true">
							<option ng-repeat="x in ctrlBook.searchingCriteria">{{x}}</option>
						</select>
					</div>
				</div>
			</div>

			<div class="table-responsive">
				<table class="table table-hover">
					<thead>
						<tr>
							<th>ID</th>
							<th>AUTHOR</th>
							<th>TITLE</th>
							<th>ISBN</th>
							<th width="100"></th>
							<th width="100"></th>
						</tr>
					</thead>
					<tbody>
						<tr ng-repeat="b in ctrlBook.getAllBooks()">
							<td>{{b.id}}</td>
							<td>{{b.authorName}}</td>
							<td>{{b.bookTitle}}</td>
							<td>{{b.isbn}}</td>
							<td><button type="button" ng-click="ctrlBook.editBook(b.id)" class="btn btn-success custom-width">Edit</button></td>
							<td><button type="button" ng-click="ctrlBook.removeBook(b.id)" class="btn btn-danger custom-width">Remove</button></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>