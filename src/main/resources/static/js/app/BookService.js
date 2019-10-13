'use strict';

angular.module('bookApp').factory('BookService',
    ['$localStorage', '$http', '$q', 'urls', '$compile', '$rootScope',
        function ($localStorage, $http, $q, urls, $compile, $rootScope) {

            var factory = {
            		testAlert: testAlert,
            		myLog: myLog,
            		loadAllBooks: loadAllBooks,
                getAllBooks: getAllBooks,
                getBook: getBook,
                createBook: createBook,
                updateBook: updateBook,
  							updateBookAndFiles: updateBookAndFiles,
                removeBook: removeBook,
                removeFile: removeFile,
                searchBooksByTitle: searchBooksByTitle,
                searchBooksByAuthor: searchBooksByAuthor,
                searchBooksByIsbn: searchBooksByIsbn,
                getSearchedBooks: getSearchedBooks,
                getJsonModel: getJsonModel
            };

            return factory;

            function testAlert(menu) {
            	alert('test ' + menu.url)
            }
            function myLog(text) {
            	console.log(text);
            }
            
            function removeBook(id) {
              console.log('Removing Book with id ' + id);
              var deferred = $q.defer();
              $http.delete(urls.BOOK_SERVICE_API + id)
                  .then(
                      function (response) {
                          loadAllBooks();
                          deferred.resolve(response.data);
                      },
                      function (errResponse) {
                          console.error('Error while removing Book with id :' + id);
                          deferred.reject(errResponse);
                      }
                  );
              return deferred.promise;
            }
            
            function searchBooksByTitle(searchedTitle) {

                console.log('Searching books starting with: ' + searchedTitle);
                var deferred = $q.defer();
                // console.log('url = ' + urls.BOOK_SERVICE_API + "title/" + searchedTitle);

                $http.get(urls.BOOK_SERVICE_API + "title/" + searchedTitle)
                    .then(
                        function (response) {
                            console.log('Fetched successfully all searched books');
                            $localStorage.searchedBooks = response.data;
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while searching books')
                            deferred.resolve(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function searchBooksByAuthor(searchedAuthor) {

                console.log('Searching books with author: ' + searchedAuthor);
                var deferred = $q.defer();
                // console.log('url = ' + urls.BOOK_SERVICE_API + "author/" + searchedAuthor);

                $http.get(urls.BOOK_SERVICE_API + "author/" + searchedAuthor)
                    .then(
                        function (response) {
                            console.log('Fetched successfully all searched books');
                            $localStorage.searchedBooks = response.data;
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while searching books')
                            deferred.resolve(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function searchBooksByIsbn(searchedIsbn) {

                console.log('Searching books by ISBN: ' + searchedIsbn);
                var deferred = $q.defer();
                //console.log('url = ' + urls.BOOK_SERVICE_API + "isbn/" + searchedIsbn);

                $http.get(urls.BOOK_SERVICE_API + "isbn/" + searchedIsbn)
                    .then(
                        function (response) {
                            console.log('Fetched successfully all searched books');
                            $localStorage.searchedBooks = response.data;
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while searching books')
                            deferred.resolve(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function getSearchedBooks() {
                return $localStorage.searchedBooks;
            }

            function loadAllBooks() {
                console.log('Fetching all books');

                var deferred = $q.defer();
                // $http.get(urls.BOOK_SERVICE_API, $localStorage.headersConfig)
                $http.get(urls.BOOK_SERVICE_API)
                    .then(
                        function (response) {
                            //console.log('Fetched successfully all books');
                            $localStorage.books = response.data;
                            //console.log('Totally fetched books#: ' + $localStorage.books.length);
                            deferred.resolve(response);
                        },
                        function (errResponse) {
                            console.error('Error while loading books');
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function getAllBooks() {
                //console.log('Totally fetched books#: ' + $localStorage.books.length);
                return $localStorage.books;
            }

            function getBook(bookId) {
                console.log('Fetching Book with id :' + bookId);
                var deferred = $q.defer();
                var url = urls.BOOK_SERVICE_API + bookId + '/files';
                //console.log('url = ' + url);
                // <from file-demo main.js>
                var multipleFileUploadError = document.querySelector('#multipleFileUploadError');
                var multipleFileUploadSuccess = document.querySelector('#multipleFileUploadSuccess');
                // </from file-demo main.js>

                // $http.get(urls.BOOK_SERVICE_API + id)
                $http.get(url)
                    .then(
                        function (response) {
                            console.log('Fetched successfully Book with id :' + bookId);

                            // get book from response:
                            deferred.resolve(response.data.book);
                            if (response.data.fileResponse.length > 0) {
                                multipleFileUploadError.style.display = "none";
                                var content = "<p>List of book files:</p>";
                                var fileId = "";
                                var fileDownloadUri = "";
                                var fileName = "";
                                for (var i = 0; i < response.data.fileResponse.length; i++) {
                                    fileId = response.data.fileResponse[i].id;
                                    fileDownloadUri = response.data.fileResponse[i].fileDownloadUri;
                                    fileName = response.data.fileResponse[i].fileName;
                                    
                                    content += "<div class='row'>"; 
                                    content += "<div class='form-group col-md-8'>DownloadUrl : <a href='" + fileDownloadUri + "' target='_blank'>" + fileName + "</a></div>"; 
                                    content += `<div ng-controller='BookController as bc' class='form-group col-md-4'><button type="button" ng-click="bc.removeFile('${bookId}', '${fileId}')" class="btn btn-danger btn-sm">Delete</button></div>`;
                                    content += "</div>";                                    
                                }
                                multipleFileUploadSuccess.innerHTML = content;
                                $compile(multipleFileUploadSuccess)($rootScope);
                                multipleFileUploadSuccess.style.display = "block";
                            } else {
                              multipleFileUploadSuccess.innerHTML = "";                            	
                            }
                            
                        },
                        function (errResponse) {

                            multipleFileUploadSuccess.style.display = "none";
                            multipleFileUploadError.innerHTML = (response && response.message) || "Some Error Occurred";

                            console.error('Error while loading book with id :' + id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function getJsonModel(book) {
                var bookModel = {
                		id: book.id,
                    authorName: book.authorName,
                    bookTitle: book.bookTitle,
                    isbn: book.isbn
                }
                return bookModel;
            }

            function createBook(book) {
                console.log('Creating Book');
                var deferred = $q.defer();
                var url = urls.BOOK_SERVICE_API + 'files';
                console.log('bookModel = ' + angular.toJson(bookModel));
                // http.post FormData comprised of:
                // - bookModel - json object (a string version)
                // - book.files - multipart files

                // Fill FormData with bookModel and files
                var bookModel = getJsonModel(book);
                var formData = new FormData();
                formData.append("model", angular.toJson(bookModel));
								console.log('???$localStorage.files#: ' + $localStorage.files.length);
                if ($localStorage.files.length > 0) {
                    for (var i = 0; i < $localStorage.files.length; i++) {
                        formData.append('file' + i, $localStorage.files[i]);
                        console.log('file_' + i);
                    }
                }

                $http.post(url, formData, {
                    headers: { 'Content-Type': undefined },
                    transformRequest: angular.identity
                })
                    .then(
                        function (response) {
                        	// clean input file element of myForm:
                        	var fileUpload = angular.element(document.getElementById("file_upload"));
                        	angular.element(fileUpload).val(null);                        	
                        	loadAllBooks();
                          deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while creating Book : ' + errResponse.data.errorMessage);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updateBook(book, id) {
                console.log('Updating Book with id ' + id);
                var deferred = $q.defer();
                $http.put(urls.BOOK_SERVICE_API + id, book)
                    .then(
                        function (response) {
                            loadAllBooks();
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while updating Book with id :' + id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function updateBookAndFiles(book, id) {
              console.log('Updating Book with id ' + id);
              var deferred = $q.defer();
              var url = urls.BOOK_SERVICE_API +  + id +'/files';

              // Fill FormData with bookModel and files
              var bookModel = getJsonModel(book);
              var formData = new FormData();
              formData.append("model", angular.toJson(bookModel));
              console.log('bookModel = ' + angular.toJson(bookModel));
							console.log('???$localStorage.files#: ' + $localStorage.files.length);
              if ($localStorage.files.length > 0) {
                  for (var i = 0; i < $localStorage.files.length; i++) {
                      formData.append('file' + i, $localStorage.files[i]);
                      console.log('file_' + i);
                  }
              }

              $http.put(url, formData, {
                  headers: { 'Content-Type': undefined },
                  transformRequest: angular.identity
              })
                  .then(
                      function (response) {
												// after update:
                      	// clean input file element of myForm:
                      	var fileUpload = angular.element(document.getElementById("file_upload"));
      									angular.element(fileUpload).val(null);                      	

                          loadAllBooks();	// refresh list
                          getBook(id);		// refresh book form
                          deferred.resolve(response.data);
                      },
                      function (errResponse) {
                          console.error('Error while updating Book with files: ' + errResponse.data.errorMessage);
                          deferred.reject(errResponse);
                      }
                  );
              return deferred.promise;
            }
            
            function removeFile(bookId, fileId) {
                console.log('Removing File with id: ' + fileId);
                var deferred = $q.defer();
                //$http.delete(urls.FILE_SERVICE_API + fileId)
                $http.delete(urls.BOOK_SERVICE_API + bookId + "/files/" + fileId)
                    .then(
                        function (response) {
                            getBook(bookId);
                            deferred.resolve(response.data);
                        },
                        function (errResponse) {
                            console.error('Error while removing File with id :' + fileId);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }
            
            // <from file-demo main.js>
            multipleUploadForm.addEventListener('submit', function (event) {
                var files = multipleFileUploadInput.files;
                if (files.length === 0) {
                    multipleFileUploadError.innerHTML = "Please select at least one file";
                    multipleFileUploadError.style.display = "block";
                }
                uploadMultipleFiles(files);
                event.preventDefault();
            }, true);
            // </from file-demo main.js>
        }
    ]);