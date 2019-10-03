'use strict';

angular.module('bookApp').factory('BookService',
    ['$localStorage', '$http', '$q', 'urls',
        function ($localStorage, $http, $q, urls) {

            var factory = {
                loadAllBooks: loadAllBooks,
                getAllBooks: getAllBooks,
                getBook: getBook,
                createBook: createBook,
                updateBook: updateBook,
                removeBook: removeBook,
                searchBooksByTitle: searchBooksByTitle,
                searchBooksByAuthor: searchBooksByAuthor,
                searchBooksByIsbn: searchBooksByIsbn,
                getSearchedBooks: getSearchedBooks,
                getJsonModel: getJsonModel
            };

            return factory;

            function searchBooksByTitle(searchedTitle) {

                console.log('Searching books starting with: ' + searchedTitle);
                var deferred = $q.defer();
                console.log('url = ' + urls.BOOK_SERVICE_API + "title/" + searchedTitle);

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
                console.log('url = ' + urls.BOOK_SERVICE_API + "author/" + searchedAuthor);

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
                console.log('url = ' + urls.BOOK_SERVICE_API + "isbn/" + searchedIsbn);

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
                            console.log('Fetched successfully all books');
                            $localStorage.books = response.data;
                            console.log('Totally fetched books#: ' + $localStorage.books.length);
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
                console.log('Totally fetched books#: ' + $localStorage.books.length);
                return $localStorage.books;
            }

            function getBook(id) {
                console.log('Fetching Book with id :' + id);
                var deferred = $q.defer();
                var url = urls.BOOK_SERVICE_API + id + '/files';
                console.log('url = ' + url);
                // <from file-demo main.js>
                var multipleFileUploadError = document.querySelector('#multipleFileUploadError');
                var multipleFileUploadSuccess = document.querySelector('#multipleFileUploadSuccess');
                // </from file-demo main.js>

                // $http.get(urls.BOOK_SERVICE_API + id)
                $http.get(url)
                    .then(
                        function (response) {
                            console.log('Fetched successfully Book with id :' + id);

                            // get book from response:
                            deferred.resolve(response.data.book);
                            console.log('response.status :' + response.status);
                            console.log('response.data :' + response.data);
                            console.log('response.data.book :' + response.data.book.authorName);
                            console.log('response.data.fileResponse :' + response.data.fileResponse);
                            console.log('response.data.fileResponse.length :' + response.data.fileResponse.length);

                            // <from file-demo main.js>
                            if (response.data.fileResponse.length > 0) {
                                multipleFileUploadError.style.display = "none";
                                var content = "<p>List of book files:</p>";
                                for (var i = 0; i < response.data.fileResponse.length; i++) {
                                    content += "<p>DownloadUrl : <a href='" + response.data.fileResponse[i].fileDownloadUri + "' target='_blank'>" + response.data.fileResponse[i].fileDownloadUri + "</a></p>";
                                }
                                multipleFileUploadSuccess.innerHTML = content;
                                multipleFileUploadSuccess.style.display = "block";
                            }
                            // </from file-demo main.js>
                        },
                        function (errResponse) {

                            // <from file-demo main.js>
                            multipleFileUploadSuccess.style.display = "none";
                            multipleFileUploadError.innerHTML = (response && response.message) || "Some Error Occurred";
                            // </from file-demo main.js>

                            console.error('Error while loading book with id :' + id);
                            deferred.reject(errResponse);
                        }
                    );
                return deferred.promise;
            }

            function getJsonModel(book) {
                var bookModel = {
                    authorName: book.authorName,
                    bookTitle: book.bookTitle,
                    isbn: book.isbn
                }
                return bookModel;
            }

            function createBook(book) {
                console.log('Creating Book');
                var deferred = $q.defer();
                var url = urls.BOOK_SERVICE_API + 'upload';
                console.log('bookModel = ' + angular.toJson(bookModel));
                console.log('$localStorage.files#: ' + $localStorage.files.length);
                console.log('url = ' + url);
                // http.post FormData comprised of:
                // - bookModel - json object (a string version)
                // - book.files - multipart files

                // Fill FormData with bookModel and files
                var bookModel = getJsonModel(book);
                var formData = new FormData();
                formData.append("model", angular.toJson(bookModel));
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
                            console.log('in response');
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