package com.example.vigil.ui.screens

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vigil.R
import com.example.vigil.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeveloperProfileScreen(onBack: () -> Unit) {
    val scrollState = rememberScrollState()
    val context = LocalContext.current

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = { Text("About The Developer", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
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
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            // Premium Profile Image with local resource
            GlowProfileImage()

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Prince Yadav",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Black,
                color = Color.White
            )

            Text(
                text = "Agentic AI Developer",
                style = MaterialTheme.typography.titleMedium,
                color = AccentPrimary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "I build AI agents, full-stack applications,\nautomation systems, developer tools,\nand experimental software products.",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                color = Color.White.copy(alpha = 0.8f),
                lineHeight = 26.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Premium Action Cards
            PremiumLinkCard(
                title = "Follow on Instagram",
                icon = Icons.Default.CameraAlt,
                gradient = listOf(Color(0xFF833AB4), Color(0xFFFD1D1D), Color(0xFFFCB045)),
                onClick = { openUrl(context, "https://www.instagram.com/prince.yadav______/") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            PremiumLinkCard(
                title = "Explore My Projects",
                icon = Icons.Default.Code,
                gradient = listOf(Color(0xFF24292E), Color(0xFF4078C0)),
                onClick = { openUrl(context, "https://github.com/prince-up") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            PremiumLinkCard(
                title = "Connect on LinkedIn",
                icon = Icons.Default.Person,
                gradient = listOf(Color(0xFF0077B5), Color(0xFF00A0DC)),
                onClick = { openUrl(context, "https://www.linkedin.com/in/prince-yadav-4t/") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            PremiumLinkCard(
                title = "☕ Buy Me A Coffee",
                icon = Icons.Default.Favorite,
                gradient = listOf(Color(0xFFFFDD00), Color(0xFFF4B400)),
                onClick = { openUrl(context, "https://github.com/prince-up") }
            )

            Spacer(modifier = Modifier.height(64.dp))

            Text(
                text = "Built by Prince",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.5f)
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun GlowProfileImage() {
    val infiniteTransition = rememberInfiniteTransition(label = "glow")
    
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ), label = "rotation"
    )

    val glowScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ), label = "glow"
    )

    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(220.dp)) {
        // Outer Glow
        Box(
            modifier = Modifier
                .size(180.dp)
                .drawBehind {
                    drawCircle(
                        Brush.radialGradient(
                            listOf(AccentPrimary.copy(alpha = 0.4f), Color.Transparent),
                            radius = (size.minDimension / 2) * glowScale
                        )
                    )
                }
        )

        // Animated Gradient Border
        Box(
            modifier = Modifier
                .size(190.dp)
                .rotate(rotation)
                .border(
                    width = 4.dp,
                    brush = Brush.sweepGradient(listOf(AccentPrimary, AccentSecondary, AccentTertiary, AccentPrimary)),
                    shape = CircleShape
                )
        )

        // Main Image
        Image(
            painter = painterResource(id = R.drawable.princeimg),
            contentDescription = "Prince Yadav",
            modifier = Modifier
                .size(180.dp)
                .clip(CircleShape)
                .border(2.dp, GlassBorder, CircleShape),
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun PremiumLinkCard(
    title: String,
    icon: ImageVector,
    gradient: List<Color>,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable { onClick() }
            .border(1.dp, GlassBorder, RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Brush.linearGradient(gradient)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(24.dp))
            }
            
            Spacer(modifier = Modifier.width(20.dp))
            
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            Icon(
                Icons.Default.ChevronRight,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.3f)
            )
        }
    }
}

private fun openUrl(context: android.content.Context, url: String) {
    try {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        context.startActivity(intent)
    } catch (e: Exception) {
        Log.e("DeveloperProfile", "Failed to open URL: $url", e)
        Toast.makeText(context, "Could not open link", Toast.LENGTH_SHORT).show()
    }
}
