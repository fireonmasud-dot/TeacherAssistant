package com.teacherassistant.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.teacherassistant.app.ui.academic.ExamRankingScreen
import com.teacherassistant.app.ui.academic.ResultEntryScreen
import com.teacherassistant.app.ui.ai.AiAssistantScreen
import com.teacherassistant.app.ui.dashboard.DashboardScreen
import com.teacherassistant.app.ui.login.LoginScreen
import com.teacherassistant.app.ui.profile.StudentProfileScreen
import com.teacherassistant.app.ui.profile.TeacherProfileScreen
import com.teacherassistant.app.ui.studentlist.AddStudentScreen
import com.teacherassistant.app.ui.studentlist.StudentListScreen
import com.teacherassistant.app.ui.tracking.MonthlyTiffinReportScreen
import com.teacherassistant.app.ui.tracking.TiffinTrackerScreen
import com.teacherassistant.app.ui.utility.ClassRoutineScreen
import com.teacherassistant.app.ui.utility.RecentlyDeletedScreen
import com.teacherassistant.app.ui.utility.TodoListScreen

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "login") {
        
        composable("login") {
            LoginScreen(
                onLoginSuccess = { 
                    navController.navigate("dashboard") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }

        composable("dashboard") {
            DashboardScreen(
                onNavigateToStudents = { navController.navigate("student_list") },
                onNavigateToTiffinTracker = { navController.navigate("tiffin_tracker") },
                onNavigateToMonthlyReport = { navController.navigate("monthly_tiffin_report") },
                onNavigateToResultEntry = { navController.navigate("result_entry") },
                onNavigateToExamRanking = { navController.navigate("exam_ranking") },
                onNavigateToRoutine = { navController.navigate("routine") },
                onNavigateToTodos = { navController.navigate("todos") },
                onNavigateToDeleted = { navController.navigate("deleted_students") },
                onNavigateToAiAssistant = { navController.navigate("ai_assistant") },
                onNavigateToTeacherProfile = { navController.navigate("teacher_profile") }
            )
        }

        composable("student_list") {
            StudentListScreen(
                onNavigateToProfile = { studentId -> navController.navigate("student_profile/$studentId") },
                onNavigateToAddStudent = { navController.navigate("add_student") }
            )
        }

        composable("add_student") {
            AddStudentScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "student_profile/{studentId}",
            arguments = listOf(navArgument("studentId") { type = NavType.LongType })
        ) {
            StudentProfileScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable("tiffin_tracker") {
            TiffinTrackerScreen()
        }

        composable("monthly_tiffin_report") {
            MonthlyTiffinReportScreen(
                onStudentClick = { studentId -> navController.navigate("student_profile/$studentId") }
            )
        }

        composable("result_entry") {
            ResultEntryScreen()
        }

        composable("exam_ranking") {
            ExamRankingScreen()
        }

        composable("routine") {
            ClassRoutineScreen()
        }

        composable("todos") {
            TodoListScreen()
        }

        composable("deleted_students") {
            RecentlyDeletedScreen()
        }

        composable("ai_assistant") {
            AiAssistantScreen()
        }

        composable("teacher_profile") {
            TeacherProfileScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
