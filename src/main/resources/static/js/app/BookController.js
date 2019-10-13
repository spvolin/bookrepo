'use strict';

angular.module('bookApp').controller('apiCtrl', function apiCtrl($scope) {
	$scope.menus = [ {
		menu : 'page_01',
		url : 'page_01.html'
	}, {
		menu : 'page_02',
		url : 'page_02.html'
	} ];

	$scope.test = function(menu) {
		alert('test ' + menu.url)
	}
})
		
angular.module('bookApp').controller('BookController', [
						'BookService',
						'$scope',
						'$rootScope',
						'$localStorage',
						'$compile',
						'$document',
						function(BookService, $scope, $rootScope, $localStorage, $compile, $document) {

							console.log('#1 In BookController');
							var self = this;
							
							// <TEST BLOCK>
							$scope.menus = [ {
								menu : 'page_01',
								url : 'page_01.html'
							}, {
								menu : 'page_02',
								url : 'page_02.html'
							} ];

							// Ok:
//							self.test = function(menu) {
//								alert('test ' + menu.url)
//							}
							
							// Test:
							self.test = test;
							function test(menu) {
								console.log('Alerting...');
								BookService.testAlert(menu);
							}
							
							self.mylog = mylog;
							self.alert = alert;
							
							function mylog(text) {
								BookService.myLog(text);
							};
							function myalert(text) {
								alert(text);
							};				
							// </TEST BLOCK>
							
							
							self.book = {};
							self.books = [];
							self.searchedString;
							self.searchingCriteria = [ "Title", "Author", "ISBN" ];
							self.searchedCriterion;

							// console.log('$rootScope.username: ' +
							// $rootScope.username);
							// console.log('$scope.username: ' +
							// $scope.username);
							//        
							// console.log('$localStorage.currentUser: ' +
							// $localStorage.currentUser.username);
							$scope.username = $localStorage.currentUser.username;
							console.log('$scope.username: ' + $scope.username);

							self.submit = submit;
							self.getAllBooks = getAllBooks;
							self.createBook = createBook;
							self.updateBook = updateBook;
							self.updateBookAndFiles = updateBookAndFiles;
							self.removeBook = removeBook;
							self.removeFile = removeFile;
							self.editBook = editBook;
							// self.searchBooksByAuthor = searchBooksByAuthor;
							// self.searchBooksByTitle = searchBooksByTitle;
							// self.searchBooksByAuthor = searchBooksByAuthor;
							// self.searchBooksByIsbn = searchBooksByIsbn;
							self.reset = reset;

							self.successMessage = '';
							self.errorMessage = '';
							self.done = false;

							self.onlyIntegers = /^\d+$/;
							self.onlyNumbers = /^\d+([,.]\d+)?$/;

							// <FILE>
							// an array of files selected
							$scope.files = [];
							$localStorage.files = [];
							$scope.nmOfFiles = 0;
							// listen for the file selected event
							$scope.$on("fileSelected", function(event, args) {
								$scope.$apply(function() {
									// add the file object to the scope's files
									// collection
									//console.log('args.file = ' + args.file);
									$scope.files.push(args.file);
									$scope.nmOfFiles++;
								});
							});
							$localStorage.files = $scope.files;
							console.log('$scope.nmOfFiles: ' + $scope.nmOfFiles);
							console.log('$localStorage.files>: ' + $localStorage.files.length);
							console.log('$scope.files>: ' + $scope.files.length);
							
							//Part 2: enables Update button if files selected 
					    $scope.inputContainsFile = false;
					    $scope.filesSelected = function(element)
					    {
					        if(element.files.length > 0)
					          $scope.inputContainsFile = true;
					        else
					          $scope.inputContainsFile = false;
					    }
					    console.log('$scope.inputContainsFile: ' + $scope.inputContainsFile);
							// </FILE>

							// console.log('#2 In BookController');

							$scope.checkFiles = function() {
								 if (book.files.length > 1) { 
								   return true;
								  }
								  else {
								   return false;
								  }
							};
								
							function submit() {

								console.log('Submitting');

								if (self.book.id === undefined || self.book.id === null) {

									console.log('Saving New Book', self.book);
									console.log('$scope.files#: ' + $scope.files.length);
									$localStorage.files = $scope.files;
									console.log('$localStorage.files#: ' + $localStorage.files.length);

									createBook(self.book);

								} else {
									//updateBook(self.book, self.book.id);
									//console.log('Book updated with id ', self.book.id);
									console.log('Updating Book', self.book);
									console.log('$scope.files#: ' + $scope.files.length);
									$localStorage.files = $scope.files;
									updateBookAndFiles(self.book, self.book.id);
									console.log('Book with files updated with id ', self.book.id);
									// clean list of displayed files after upload 
									// (see also nulling of id="file_upload" in BookService.updateBookAndFiles())
									$scope.files = [];
								}
							}

							function reset() {
								// console.log('#4 In BookController');
								self.successMessage = '';
								self.errorMessage = '';
								self.book = {};
								var fileElement = angular.element(document.getElementById("file_upload"));
								angular.element(fileElement).val(null);
								$scope.myForm.$setPristine(); // reset Form
								$scope.myForm.file =''; // reset Form
								self.book.files = '';
							}

							function createBook(book) {
								console.log('About to create book');
								console.log('$scope.files#: ' + $scope.files.length);
								console.log('$localStorage.files#: ' + $localStorage.files.length);

								BookService.createBook(book).then(
										function(response) {
											console.log('Book created successfully');
											self.successMessage = 'Book created successfully';
											self.errorMessage = '';
											self.done = true;
											self.book = {};
											$scope.myForm.file = '';
											$scope.myForm.$setPristine();
										},
										function(errResponse) {
											console.error('Error while creating Book');
											self.errorMessage = 'Error while creating Book: '
													+ errResponse.data.errorMessage;
											self.successMessage = '';
										});
							}

							function updateBook(book, id) {
								console.log('About to update book');
								BookService.updateBook(book, id).then(
										function(response) {
											console.log('Book updated successfully');
											self.successMessage = 'Book updated successfully';
											self.errorMessage = '';
											self.done = true;
											$scope.myForm.$setPristine();
										},
										function(errResponse) {
											console.error('Error while updating Book');
											self.errorMessage = 'Error while updating Book '
													+ errResponse.data;
											self.successMessage = '';
										});
							}

							function updateBookAndFiles(book, id) {
								console.log('About to update book');
								BookService.updateBookAndFiles(book, id).then(
										function(response) {
											console.log('Book with files updated successfully');
											self.successMessage = 'Book with files updated successfully';
											self.errorMessage = '';
											self.done = true;
											$scope.myForm.$setPristine();
										},
										function(errResponse) {
											console.error('Error while updating Book with files');
											self.errorMessage = 'Error while updating Book with files '
													+ errResponse.data;
											self.successMessage = '';
										});
							}

							function removeBook(id) {
								console.log('About to remove Book with id ' + id);
								BookService.removeBook(id).then(
										function() {
											console.log('Book ' + id + ' removed successfully');
										},
										function(errResponse) {
											console.error('Error while removing book ' + id
													+ ', Error :' + errResponse.data);
										});
							}

							function removeFile(bookId, fileId) {
								console.log('About to remove File ' + fileId + ' of Book '
										+ bookId);
								BookService.removeFile(bookId, fileId).then(
										function() {
											console.log('File ' + fileId + ' removed successfully');
										},
										function(errResponse) {
											console.error('Error while removing book ' + id
													+ ', Error :' + errResponse.data);
										});
							}

							function getAllBooks() {
								// return BookService.getAllBooks();
								//console.log('selfsearchedString = ' + self.searchedString);
								if (self.searchedString === undefined
										|| self.searchedString === "") {
									return BookService.getAllBooks();
								} else {
									if (self.searchBooks) {
										self.searchBooks = false;
										self.successMessage = "";
										self.errorMessage = "";

										switch (self.searchedCriterion) {
										case "Title":
											searchBooksByTitle();
											break;
										case "Author":
											searchBooksByAuthor();
											break;
										case "ISBN":
											searchBooksByIsbn();
											break;
										default:
											searchBooksByTitle();
											break;
										}

									}
									return BookService.getSearchedBooks();
								}
							}

							function searchBooksByTitle() {
								console.log('Searching book');
								BookService.searchBooksByTitle(self.searchedString).then(
										function(response) {
										}, function(errResponse) {
										});
							}

							function searchBooksByAuthor() {
								console.log('Searching book');
								BookService.searchBooksByAuthor(self.searchedString).then(
										function(response) {
										}, function(errResponse) {
										});
							}

							function searchBooksByIsbn() {
								console.log('Searching book');
								BookService.searchBooksByIsbn(self.searchedString).then(
										function(response) {
										}, function(errResponse) {
										});
							}

							function editBook(id) {
								self.successMessage = '';
								self.errorMessage = '';
								BookService.getBook(id).then(
										function(book) {
											self.book = book;
										},
										function(errResponse) {
											console.error('Error while removing book ' + id
													+ ', Error :' + errResponse.data);
										});
							}
						} ]);