package com.example.vigil.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vigil.data.database.CreatorStats
import com.example.vigil.ui.theme.*
import com.example.vigil.ui.viewmodels.DashboardViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel(),
    onNavigateToDeveloper: () -> Unit,
    onNavigateToDebug: () -> Unit
) {
    val todayStats by viewModel.todayStats.collectAsState()
    val isAccessibilityEnabled by viewModel.isAccessibilityEnabled.collectAsState()
    val weeklyStats by viewModel.weeklyStats.collectAsState()
    val topCreators by viewModel.topCreators.collectAsState()

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "ReelGuard",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "KNOW YOUR TIME",
                            style = MaterialTheme.typography.labelSmall,
                            color = AccentSecondary,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToDebug) {
                        Icon(Icons.Default.BugReport, contentDescription = "Debug", tint = AccentPrimary)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Hero Section: Animated Count
            HeroStatsCard(
                reelsCount = todayStats?.reelCount ?: 0
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Accessibility Status: Modern Pill
            AccessibilityStatusPill(isAccessibilityEnabled)

            Spacer(modifier = Modifier.height(32.dp))

            // Section Title: Progress
            SectionHeader(title = "Productivity Goal")
            Spacer(modifier = Modifier.height(12.dp))
            DailyGoalSection(currentCount = todayStats?.reelCount ?: 0)

            if (topCreators.isNotEmpty()) {
                Spacer(modifier = Modifier.height(32.dp))
                // Top Creators Section: Premium List
                TopCreatorsAnalyticsCard(topCreators)
            }

            if (weeklyStats.isNotEmpty()) {
                Spacer(modifier = Modifier.height(32.dp))
                // Weekly Trend: Minimalist Chart
                WeeklyTrendChart(weeklyStats)
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Footer: Personal Branding
            DeveloperFooter(onClick = onNavigateToDeveloper)

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
fun SectionHeader(title: String, action: String? = null) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
        if (action != null) {
            Text(
                text = action,
                style = MaterialTheme.typography.labelLarge,
                color = AccentPrimary,
                fontWeight = FontWeight.ExtraBold
            )
        }
    }
}

@Composable
fun HeroStatsCard(reelsCount: Int) {
    val counter by animateIntAsState(
        targetValue = reelsCount,
        animationSpec = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
        label = "count"
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(32.dp))
            .background(
                Brush.verticalGradient(
                    listOf(AccentPrimary, AccentTertiary)
                )
            )
    ) {
        // Decorative Circle
        Box(
            modifier = Modifier
                .size(150.dp)
                .offset(x = 180.dp, y = (-50).dp)
                .background(Color.White.copy(alpha = 0.1f), CircleShape)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(28.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "REELS TODAY",
                style = MaterialTheme.typography.labelMedium,
                color = Color.White.copy(alpha = 0.8f),
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Text(
                text = counter.toString(),
                style = MaterialTheme.typography.displayLarge,
                fontWeight = FontWeight.Black,
                color = Color.White,
                fontSize = 80.sp
            )
        }
    }
}

@Composable
fun AccessibilityStatusPill(enabled: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "alpha"
    )

    Surface(
        color = DarkSurface,
        shape = CircleShape,
        border = BorderStroke(1.dp, if (enabled) SuccessGreen.copy(alpha = 0.5f) else ErrorRed.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(vertical = 14.dp, horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(if (enabled) SuccessGreen else ErrorRed)
                    .padding(2.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .background((if (enabled) SuccessGreen else ErrorRed).copy(alpha = alpha))
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = if (enabled) "TRACKING ENGINE ACTIVE" else "TRACKING ENGINE DISABLED",
                color = if (enabled) SuccessGreen else ErrorRed,
                fontWeight = FontWeight.Black,
                style = MaterialTheme.typography.labelMedium,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                imageVector = if (enabled) Icons.Default.CheckCircle else Icons.Default.Error,
                contentDescription = null,
                tint = if (enabled) SuccessGreen else ErrorRed,
                modifier = Modifier.size(18.dp)
            )
        }
    }
}

@Composable
fun DailyGoalSection(currentCount: Int) {
    val goal = 100
    val progress = (currentCount.toFloat() / goal).coerceIn(0f, 1f)
    
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(DarkSurface)
            .border(1.dp, GlassBorder, RoundedCornerShape(16.dp))
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Progress to limit",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.6f)
            )
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .width(150.dp)
                    .height(8.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.1f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(progress)
                        .clip(CircleShape)
                        .background(AccentSecondary)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                "$currentCount",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun TopCreatorsAnalyticsCard(creators: List<CreatorStats>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, GlassBorder, RoundedCornerShape(28.dp)),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "🔥 TOP CREATORS",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Black,
                    color = AccentSecondary,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(Icons.Default.Stars, contentDescription = null, tint = WarningOrange, modifier = Modifier.size(20.dp))
            }
            
            Spacer(modifier = Modifier.height(20.dp))

            creators.forEachIndexed { index, creator ->
                CreatorAnalyticsRow(rank = index + 1, creator = creator)
                if (index < creators.size - 1) {
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 16.dp),
                        color = Color.White.copy(alpha = 0.05f)
                    )
                }
            }
        }
    }
}

@Composable
fun CreatorAnalyticsRow(rank: Int, creator: CreatorStats) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Rank / Icon
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(
                    if (rank == 1) Brush.linearGradient(listOf(AccentPrimary, AccentTertiary))
                    else Brush.linearGradient(listOf(DarkSurfaceVariant, DarkSurfaceVariant))
                ),
            contentAlignment = Alignment.Center
        ) {
            if (rank == 1) {
                Text("👑", fontSize = 18.sp)
            } else {
                Text(
                    text = "#$rank",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = creator.username,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Text(
                text = "${creator.watchCount} reels",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White.copy(alpha = 0.5f)
            )
        }

        // Mini Trend Icon
        Icon(
            imageVector = Icons.Default.TrendingUp,
            contentDescription = null,
            tint = if (rank == 1) SuccessGreen else Color.White.copy(alpha = 0.1f),
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
fun WeeklyTrendChart(stats: List<com.example.vigil.data.database.DailyStats>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = BorderStroke(1.dp, GlassBorder)
    ) {
        Column(modifier = Modifier.padding(24.dp)) {
            Text(
                "WEEKLY ENGAGEMENT",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Black,
                color = Color.White.copy(alpha = 0.5f),
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom
            ) {
                val max = (stats.maxOfOrNull { it.reelCount } ?: 1).coerceAtLeast(1)
                
                repeat(7) { i ->
                    val stat = stats.getOrNull(i)
                    val count = stat?.reelCount ?: 0
                    val heightFactor = count.toFloat() / max
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(
                            modifier = Modifier
                                .width(14.dp)
                                .fillMaxHeight(0.8f * heightFactor)
                                .clip(RoundedCornerShape(4.dp))
                                .background(
                                    if (i == stats.size - 1) AccentPrimary 
                                    else AccentPrimary.copy(alpha = 0.3f)
                                )
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = (stat?.date?.takeLast(2) ?: i.toString()),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White.copy(alpha = 0.3f),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DeveloperFooter(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = AccentPrimary.copy(alpha = 0.1f)),
        border = BorderStroke(1.dp, AccentPrimary.copy(alpha = 0.2f))
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "✨ BUILT BY PRINCE YADAV",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Black,
                color = Color.White,
                letterSpacing = 1.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Pushing the boundaries of Agentic AI",
                style = MaterialTheme.typography.bodySmall,
                color = Color.White.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )
        }
    }
}
