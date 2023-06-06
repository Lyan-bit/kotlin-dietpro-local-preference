package com.example.dietpro

import android.content.Context
import java.util.ArrayList
import android.graphics.Bitmap
import android.content.res.AssetManager
import org.tensorflow.lite.Interpreter


class ModelFacade private constructor(context: Context) {

    private var db: DB
    private val assetManager: AssetManager = context.assets
    private var fileSystem: FileAccessor
    private var imageClassifier: ImageClassifier

   private var currentUser: UserVO? = null
		private var currentUsers: ArrayList<UserVO> = ArrayList()
    private var currentMeal: MealVO? = null
	private var currentMeals: ArrayList<MealVO> = ArrayList()

    init {
    	//init
        db = DB(context, null)
        ModelPreferencesManager.with(context, "UserDATA")
        currentUser = getUser()
        fileSystem = FileAccessor(context)
        imageClassifier = ImageClassifier(context)
	}

    companion object {
        private var instance: ModelFacade? = null
        fun getInstance(context: Context): ModelFacade {
            return instance ?: ModelFacade(context)
        }
    }
    
    fun createMeal(x: MealVO) { 
			 db.createMeal(x)
			 currentMeal = x
	}
			    
    fun editMeal(x: MealVO) {
        db.editMeal(x)
        currentMeal = x
	}

	   fun setSelectedMeal(x: MealVO) {
		    currentMeal = x
	}
		    
		    
	 fun deleteMeal(id: String) {
			 db.deleteMeal(id)
			 currentMeal = null
	 }
			
    fun searchMealByDate(dates: String) : ArrayList<Meal> {
		val itemsList: ArrayList<Meal> = ArrayList()
			currentMeals = db.listMeal()
			for (x in currentMeals.indices) {
				if ( currentMeals[x].getDates() == dates) {
					val vo: MealVO = currentMeals[x]
				    val itemx = Meal.createByPKMeal(vo.getMealId())
					    itemx.mealId = vo.getMealId()
				    itemx.mealName = vo.getMealName()
				    itemx.calories = vo.getCalories()
				    itemx.dates = vo.getDates()
				    itemx.images = vo.getImages()
				    itemx.analysis = vo.getAnalysis()
				    itemx.userName = vo.getUserName()
					itemsList.add(itemx)
				}
			}
			return itemsList
		}
	fun searchMealByDate(): ArrayList<String> {
		currentMeals = db.listMeal()
		val res: ArrayList<String> = ArrayList()
		for (x in currentMeals.indices) {
			res.add(currentMeals[x].getDates().toString())
		}
		return res
	}
	
	fun getUser() : UserVO? {
        currentUsers.clear()
        currentUser = ModelPreferencesManager.get<User>("KEYUser")?.let { UserVO(it) }
    	currentUser?.let { currentUsers.add(0, it) }
    	return currentUser
    }
    
    fun createUser(x: UserVO) {
        ModelPreferencesManager.put(x, "KEYUser")
        currentUser = x
        currentUser?.let { currentUsers.add(0, it) }
    }
    fun setSelectedUser(x: UserVO) {
	      currentUser = x
	}
	    
    fun findTotalConsumedCaloriesByDate(meals: ArrayList<Meal>, user: User, dates: String): Double {
	      var result = 0.0
        var totalConsumedCalories: Double
          totalConsumedCalories  = 0.0
        for (meal in meals) {
	        if (meal.userName == user.userName && meal.dates == dates) {
		            totalConsumedCalories  = totalConsumedCalories + meal.calories
	        }
    }
          user.totalConsumedCalories  = totalConsumedCalories
        persistUser (user)
          result  = totalConsumedCalories
	return result
	}
	          
    fun findTargetCalories(user: User): Double {
	      var result = 0.0
          user.targetCalories  = user.calculateTargetCalories()
        persistUser (user)
          result  = user.targetCalories
	return result
	}
	          
    fun findBMR(user: User): Double {
	      var result = 0.0
          user.bmr  = user.calculateBMR()
        persistUser (user)
          result  = user.bmr
	return result
	}
	          
    fun caloriesProgress(user: User): Double {
	      var result = 0.0
        var progress: Double
          progress  = (user.totalConsumedCalories / user.targetCalories) * 100
        persistUser (user)
          result  = progress
	return result
	}
	          
    fun addUsereatsMeal(userName: String, mealId: String) {
          db.addUsereatsMeal(userName, mealId)
	    val obj = getUserByPK(userName)
        currentUser = obj?.let { UserVO(it) }
    }
    
    fun removeUsereatsMeal(userName: String, mealId: String) {
	        db.removeUsereatsMeal(userName, mealId)
	    val obj = getUserByPK(userName)
        currentUser = obj?.let { UserVO(it) }
	}
	    
    fun imageRecognition(meal: Meal ,images: Bitmap): String {
			val result = imageClassifier.recognizeImage(images)
	        meal.analysis = result[0].title  +": " + result[0].confidence
		    persistMeal(meal)
	    	return result[0].title  +": " + result[0].confidence
		}
			     


	fun listMeal(): ArrayList<MealVO> {
        currentMeals = db.listMeal()
		
        return currentMeals
	}

	fun listAllMeal(): ArrayList<Meal> {	
		currentMeals = db.listMeal()
		var res = ArrayList<Meal>()
			for (x in currentMeals.indices) {
					val vo: MealVO = currentMeals[x]
				    val itemx = Meal.createByPKMeal(vo.getMealId())
	            itemx.mealId = vo.getMealId()
            itemx.mealName = vo.getMealName()
            itemx.calories = vo.getCalories()
            itemx.dates = vo.getDates()
            itemx.images = vo.getImages()
            itemx.analysis = vo.getAnalysis()
            itemx.userName = vo.getUserName()
			res.add(itemx)
		}
		return res
	}

    fun stringListMeal(): ArrayList<String> {
        currentMeals = db.listMeal()
        val res: ArrayList<String> = ArrayList()
        for (x in currentMeals.indices) {
            res.add(currentMeals[x].toString())
        }
        return res
    }

    fun getMealByPK(value: String): Meal? {
        val res: ArrayList<MealVO> = db.searchByMealmealId(value)
	        return if (res.isEmpty()) {
	            null
	        } else {
	            val vo: MealVO = res[0]
	            val itemx = Meal.createByPKMeal(value)
            itemx.mealId = vo.getMealId()
            itemx.mealName = vo.getMealName()
            itemx.calories = vo.getCalories()
            itemx.dates = vo.getDates()
            itemx.images = vo.getImages()
            itemx.analysis = vo.getAnalysis()
            itemx.userName = vo.getUserName()
	            itemx
	        }
    }
    
    fun retrieveMeal(value: String): Meal? {
        return getMealByPK(value)
    }

    fun allMealMealIds(): ArrayList<String> {
        currentMeals = db.listMeal()
        val res: ArrayList<String> = ArrayList()
            for (meal in currentMeals.indices) {
                res.add(currentMeals[meal].getMealId())
            }
        return res
    }

    fun setSelectedMeal(i: Int) {
        if (i < currentMeals.size) {
            currentMeal = currentMeals[i]
        }
    }

    fun getSelectedMeal(): MealVO? {
        return currentMeal
    }

    fun persistMeal(x: Meal) {
        val vo = MealVO(x)
        db.editMeal(vo)
        currentMeal = vo
    }
	
	fun listUser(): ArrayList<UserVO> {
		 	getUser()
	        return currentUsers
		}
	
		fun listAllUser(): ArrayList<User> {	
			currentUsers = listUser()
			var res = ArrayList<User>()
				for (x in currentUsers.indices) {
						val vo: UserVO = currentUsers[x]
					    val itemx = User.createByPKUser(vo.getUserName())
		            itemx.userName = vo.getUserName()
            itemx.gender = vo.getGender()
            itemx.heights = vo.getHeights()
            itemx.weights = vo.getWeights()
            itemx.activityLevel = vo.getActivityLevel()
            itemx.age = vo.getAge()
            itemx.targetCalories = vo.getTargetCalories()
            itemx.totalConsumedCalories = vo.getTotalConsumedCalories()
            itemx.bmr = vo.getBmr()
				res.add(itemx)
			}
			return res
		}
	
	    fun stringListUser(): ArrayList<String> {
	        currentUsers = listUser()
	        val res: ArrayList<String> = ArrayList()
	        for (x in currentUsers.indices) {
	            res.add(currentUsers[x].toString())
	        }
	        return res
	    }
	
	    fun getUserByPK(value: String): User? {
	        val res: ArrayList<UserVO> = listUser()
		        return if (res.isEmpty()) {
		            null
		        } else {
		            val vo: UserVO = res[0]
		            val itemx = User.createByPKUser(value)
	            itemx.userName = vo.getUserName()
            itemx.gender = vo.getGender()
            itemx.heights = vo.getHeights()
            itemx.weights = vo.getWeights()
            itemx.activityLevel = vo.getActivityLevel()
            itemx.age = vo.getAge()
            itemx.targetCalories = vo.getTargetCalories()
            itemx.totalConsumedCalories = vo.getTotalConsumedCalories()
            itemx.bmr = vo.getBmr()
		            itemx
		        }
	    }
	    
	    fun retrieveUser(value: String): User? {
	        return getUserByPK(value)
	    }
	
	    fun allUserUserNames(): ArrayList<String> {
	        currentUsers = listUser()
	        val res: ArrayList<String> = ArrayList()
	            for (user in currentUsers.indices) {
	                res.add(currentUsers[user].getUserName())
	            }
	        return res
	    }

	    fun setSelectedUser(i: Int) {
	        if (i < currentUsers.size) {
	            currentUser = currentUsers[i]
	        }
	    }
	
	    fun getSelectedUser(): UserVO? {
	        return currentUser
	    }
	
	    fun persistUser(x: User) {
	        val vo = UserVO(x)
	        createUser(vo)
	        currentUser = vo
	    }
		

		
}
