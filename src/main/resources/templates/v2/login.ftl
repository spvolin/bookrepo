
<!--    ----------------------------------    BookRepo3: Login    ----------------------------------   -->

<div class="row">
	<div class="col-md-4 generic-container" style="text-align:center; margin: 5% 5%; max-width:40%;">
		<div class="panel panel-default">
			<div class="panel-heading"><span class="lead"><h3>Login</h3></span></div>
			<div class="alert alert-success" role="alert" ng-if="authCtrl.login.successMessage">{{authCtrl.login.successMessage}}</div>
			<div class="alert alert-danger" role="alert" ng-if="authCtrl.login.errorMessage">{{authCtrl.login.errorMessage}}</div>
	
			<div class="panel-body">
				<div class="formcontainer">
				
					<form ng-submit="authCtrl.login()" name="loginForm" class="form-horizontal">
				
						<div class="row">
							<div class="form-group col-md-12">
								<label class="col-md-3 control-lable" for="username">Username</label>
								<div class="col-md-9">
									<input type="text" ng-model="authCtrl.loginRequest.usernameOrEmail" id="username" class="username form-control input-sm" placeholder="Your Email Or UserName*" required ng-minlength="3"/>
								</div>
							</div>
						</div>
						
						<div class="row">
							<div class="form-group col-md-12">
								<label class="col-md-3 control-lable" for="pass">Password</label>
								<div class="col-md-9">
									<input type="password" ng-model="authCtrl.loginRequest.password" id="pass" class="form-control input-sm" placeholder="You Password*" required ng-minlength="3"/>
								</div>
							</div>
						</div>
						
						<div class="row">
							<div class="form-actions col-md-12">
								<input type="submit" value="Login" class="btn btn-primary" ng-disabled="loginForm.$invalid || loginForm.$pristine"/>
							</div>
						</div>
						
					</form>
					
				</div>
			</div>
		</div>
	</div>

	<div class="col-md-8 generic-container" style="text-align:left; margin: 5% 5%; max-width:40%;">
		<div class="panel panel-default">
			<div class="panel-heading"><span class="lead" style="text-align:center;"><h3>Register</h3></span></div>
			<div class="alert alert-success" role="alert" ng-if="authCtrl.register.successMessage">{{authCtrl.register.successMessage}}</div>
			<div class="alert alert-danger" role="alert" ng-if="authCtrl.register.errorMessage">{{authCtrl.register.errorMessage}}</div>
	
			<div class="panel-body">
				<div class="formcontainer">

					<form action="http://localhost:8082/api/auth/signup" method="POST" ng-submit="authCtrl.register()" name="signupForm" class="form-horizontal">
<!--		            <form action="http://localhost:8082/api/auth/signup" method="POST"> -->
						<div class="row">
							<div class="form-group col-md-12">
								<label class="col-md-4 control-lable" for="namerg">Your Name *</label>						
								<div class="col-md-8">
<!--				                <input type="text" name="name" id="namerg" class="form-control" placeholder="Your Name *" value="" /> -->
									<input type="text" ng-model="authCtrl.signupRequest.name" id="namerg" class="form-control input-sm" placeholder="Your Name*" value="" />
								</div>
							</div>
						</div>
						
						<div class="row">
							<div class="form-group col-md-12">
								<label class="col-md-4 control-lable" for="usernamerg">Your UserName *</label>						
								<div class="col-md-8">
<!--                    			<input type="text" name="username" id="usernamerg" class="form-control" placeholder="Your UserName *" value="" /> -->
									<input type="text" ng-model="authCtrl.signupRequest.username" id="usernamerg" class="form-control input-sm" placeholder="Your UserName*" value="" required/>
								</div>
							</div>
						</div>

						<div class="row">
							<div class="form-group col-md-12">				
								<label class="col-md-4 control-lable" for="passrg">Your Password *</label>						
								<div class="col-md-8">
<!--			                    <input type="password" name="password" id="passrg" class="form-control" placeholder="Your Password *" value="" /> -->
									<input type="password" ng-model="authCtrl.signupRequest.password" id="passrg" class="form-control input-sm" placeholder="You Password*" required ng-minlength="3"/>
								</div>
							</div>
						</div>
						
						<div class="row">
							<div class="form-group col-md-12">
								<label class="col-md-4 control-lable" for="emailrg">Your Email *</label>						
								<div class="col-md-8">
<!--			                    <input type="text" name="email" id="emailrg" class="form-control" placeholder="Your Email *" value="" /> -->
									<input type="text" ng-model="authCtrl.signupRequest.email" id="emailrg" class="form-control input-sm" placeholder="Your Email *" required ng-minlength="3"/>
								</div>
							</div>
						</div>
						
						<div class="row">
							<div class="form-actions col-md-12" style="text-align:center;">
								<input type="submit" value="Register" class="btn btn-primary" ng-disabled="signupForm.$invalid || signupForm.$pristine"/>
							</div>
						</div>
						
					</form>
				</div>
			</div>
		</div>
	</div>
</div>
