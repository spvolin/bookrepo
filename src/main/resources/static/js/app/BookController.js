'use strict';

angular.module('bookApp').controller('BookController',
    ['BookService', '$scope', '$rootScope', '$localStorage', function (BookService, $scope, $rootScope, $localStorage) {

        console.log('#1 In BookController');
        var self = this;
        self.book = {};
        self.books = [];
        self.searchedString;
        self.searchingCriteria=["Title","Author","ISBN"];  
        self.searchedCriterion;

        console.log('$rootScope.username: ' + $rootScope.username);
        console.log('$scope.username: ' + $scope.username);
        
        console.log('$localStorage.currentUser: ' + $localStorage.currentUser.username);
        $scope.username = $localStorage.currentUser.username;
        console.log('$scope.username: ' + $scope.username);
        
        self.submit = submit;
        self.getAllBooks = getAllBooks;
        self.createBook = createBook;
        self.updateBook = updateBook;
        self.removeBook = removeBook;
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
        //an array of files selected
        $scope.files = [];
        $localStorage.files = [];
        //listen for the file selected event
        $scope.$on("fileSelected", function (event, args) {
            $scope.$apply(function () {
                //add the file object to the scope's files collection
                console.log('args.file = ' + args.file);
                $scope.files.push(args.file);
            });
        });
        $localStorage.files = $scope.files;
        console.log('$scope.files>: ' + $scope.files.length);
        // </FILE>

        console.log('#2 In BookController');

        function submit() {
            
        	console.log('Submitting');
            
            if (self.book.id === undefined || self.book.id === null) {
                
            	console.log('Saving New Book', self.book);
                console.log('$scope.files#: ' + $scope.files.length);                
                createBook(self.book);
                
            } else {
                updateBook(self.book, self.book.id);
                console.log('Book updated with id ', self.book.id);
            }
        }

        function reset() {
            console.log('#4 In BookController');
            self.successMessage = '';
            self.errorMessage = '';
            self.book = {};
            $scope.myForm.$setPristine(); //reset Form
        }

        function createBook(book) {
            console.log('About to create book');
            console.log('$scope.files#: ' + $scope.files.length);

            BookService.createBook(book)
                .then(
                    function (response) {
                        console.log('Book created successfully');
                        self.successMessage = 'Book created successfully';
                        self.errorMessage = '';
                        self.done = true;
                        self.book = {};
                        $scope.myForm.$setPristine();
                    },
                    function (errResponse) {
                        console.error('Error while creating Book');
                        self.errorMessage = 'Error while creating Book: ' + errResponse.data.errorMessage;
                        self.successMessage = '';
                    }
                );
        }

        function updateBook(book, id) {
            console.log('About to update book');
            BookService.updateBook(book, id)
                .then(
                    function (response) {
                        console.log('Book updated successfully');
                        self.successMessage = 'Book updated successfully';
                        self.errorMessage = '';
                        self.done = true;
                        $scope.myForm.$setPristine();
                    },
                    function (errResponse) {
                        console.error('Error while updating Book');
                        self.errorMessage = 'Error while updating Book ' + errResponse.data;
                        self.successMessage = '';
                    }
                );
        }


        function removeBook(id) {
            console.log('About to remove Book with id ' + id);
            BookService.removeBook(id)
                .then(
                    function () {
                        console.log('Book ' + id + ' removed successfully');
                    },
                    function (errResponse) {
                        console.error('Error while removing book ' + id + ', Error :' + errResponse.data);
                    }
                );
        }

        function getAllBooks() {
            //return BookService.getAllBooks();
            console.log('selfsearchedString = ' + self.searchedString);
			if(self.searchedString===undefined || self.searchedString===""){
				return BookService.getAllBooks();
			}
			else{
				if(self.searchBooks){
					self.searchBooks = false;
					self.successMessage="";
					self.errorMessage="";
					
					switch(self.searchedCriterion){
						case "Title": searchBooksByTitle();break;
						case "Author": searchBooksByAuthor();break;
						case "ISBN": searchBooksByIsbn();break;
						default: searchBooksByTitle();break;
					}
					
				}
				return BookService.getSearchedBooks();
			}            

        }

		function searchBooksByTitle(){
			console.log('Searching book');
			BookService.searchBooksByTitle(self.searchedString)
			.then(
				function (response){
				},
				function (errResponse){
				}
			);
		}
		
		function searchBooksByAuthor(){
			console.log('Searching book');
			BookService.searchBooksByAuthor(self.searchedString)
			.then(
				function (response){
				},
				function (errResponse){
				}
			);
		}
		
		function searchBooksByIsbn(){
			console.log('Searching book');
			BookService.searchBooksByIsbn(self.searchedString)
			.then(
				function (response){
				},
				function (errResponse){
				}
			);
        }
                
        function editBook(id) {
            self.successMessage = '';
            self.errorMessage = '';
            BookService.getBook(id).then(
                function (book) {
                    self.book = book;
                },
                function (errResponse) {
                    console.error('Error while removing book ' + id + ', Error :' + errResponse.data);
                }
            );
        }
    }
    ]);