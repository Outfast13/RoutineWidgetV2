package com.routine.widget

import java.util.Calendar

data class RoutineItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val alarmHour24: Int? = null,
    val alarmMinute: Int = 0
)

object RoutineData {

    fun getDailySchedule(): List<RoutineItem> = listOf(
        RoutineItem("d_1", "3:00 - 4:00 PM • Free", "Decompress & recharge", 15, 0),
        RoutineItem("d_2", "4:00 - 5:00 PM • Calisthenics Workout", "Open Workout tab for today's split", 16, 0),
        RoutineItem("d_3", "5:30 - 7:30 PM • Study", "50m study, 10m free × 2 • Use Pomodoro bar above", 17, 30),
        RoutineItem("d_4", "8:00 - 8:15 PM • Arabic", "15m daily Arabic study", 20, 0),
        RoutineItem("d_5", "8:15 - 8:45 PM • Singing Practice", "Open Vocal tab (25m regimen + song practice)", 20, 15),
        RoutineItem("d_6", "9:00 - 9:30 PM • Accent Training", "Hindi to English • Open Accent tab", 21, 0),
        RoutineItem("d_7", "9:30 - 10:30 PM • Play Game & Enjoy", "Relax & gaming block", 21, 30),
        RoutineItem("d_8", "11:00 PM • Sleep", "Lights out for full recovery", 23, 0)
    )

    fun getWorkoutForDay(dayOfWeek: Int): Pair<String, List<RoutineItem>> {
        return when (dayOfWeek) {
            Calendar.MONDAY -> "MON: BACK, BICEPS & ABS" to listOf(
                RoutineItem("w_mon_1", "Pull-ups • 3 × 3", "3 reps × 3 sets • Full hang to chin over bar"),
                RoutineItem("w_mon_2", "Chin-ups • 3 × 2", "3 reps × 2 sets • Supinated grip, pull elbows down"),
                RoutineItem("w_mon_3", "Scapular Pull-ups • 8 × 2", "8 reps × 2 sets • Depress shoulder blades without bending elbows"),
                RoutineItem("w_mon_4", "Negative Pull-ups • 3 × 2", "3 reps × 2 sets • Slow 3-5s controlled lowering"),
                RoutineItem("w_mon_5", "Hanging Knee Raises • 10 × 3", "10 reps × 3 sets • Curl pelvis up without swinging"),
                RoutineItem("w_mon_6", "Hollow Body Hold • 20 sec × 3", "20 sec × 3 sets • Lower back glued to floor, ribs tucked")
            )
            Calendar.TUESDAY -> "TUE: CHEST, SHOULDERS & TRICEPS" to listOf(
                RoutineItem("w_tue_1", "Standard Push-ups • 10 × 3", "10 reps × 3 sets • Straight plank line, chest near floor"),
                RoutineItem("w_tue_2", "Pike Push-ups • 8 × 3", "8 reps × 3 sets • Hips high, head moves toward floor ahead of hands"),
                RoutineItem("w_tue_3", "Diamond Push-ups • 6 × 2", "6 reps × 2 sets • Close hands, elbows track back for triceps"),
                RoutineItem("w_tue_4", "Scapular Pull-ups • 12 × 2", "12 reps × 2 sets • Shoulder blade retraction/depression control"),
                RoutineItem("w_tue_5", "Plank • 30 sec × 3", "30 sec × 3 sets • Glutes & abs braced, neutral neck")
            )
            Calendar.WEDNESDAY -> "WED: RECOVERY & MOBILITY" to listOf(
                RoutineItem("w_wed_1", "Child's Pose • 30 sec × 2", "30 sec × 2 sets • Sit back on heels, breathe into lower back"),
                RoutineItem("w_wed_2", "Cat-Cow Stretch • 10 × 1", "10 reps × 1 set • Smooth spinal flexion and extension"),
                RoutineItem("w_wed_3", "Thoracic Rotations • 10 × 2", "10 reps × 2 sets • Rotate upper back without twisting hips"),
                RoutineItem("w_wed_4", "Dead Bugs • 10 × 3", "10 reps × 3 sets • Opposite arm/leg extension, low back flat"),
                RoutineItem("w_wed_5", "Light Walking • 10 min", "10 min • Easy pace for blood flow and recovery")
            )
            Calendar.THURSDAY -> "THU: BACK, BICEPS & ABS" to listOf(
                RoutineItem("w_thu_1", "Pull-ups • 3 × 3", "3 reps × 3 sets • Controlled reps, clean lockout"),
                RoutineItem("w_thu_2", "Chin-ups • 3 × 2", "3 reps × 2 sets • Palms facing you, chest up"),
                RoutineItem("w_thu_3", "Scapular Pull-ups • 8 × 2", "8 reps × 2 sets • Clean scapular control"),
                RoutineItem("w_thu_4", "Hanging Knee Raises • 10 × 3", "10 reps × 3 sets • Controlled lift and lower"),
                RoutineItem("w_thu_5", "Reverse Crunches • 12 × 3", "12 reps × 3 sets • Lift hips off floor using lower abs")
            )
            Calendar.FRIDAY -> "FRI: LEGS & CORE" to listOf(
                RoutineItem("w_fri_1", "Bodyweight Squats • 15 × 3", "15 reps × 3 sets • Knees track toes, depth with control"),
                RoutineItem("w_fri_2", "Reverse Lunges • 10 per leg × 3", "10 per leg × 3 sets • Step back, front heel grounded"),
                RoutineItem("w_fri_3", "Glute Bridges • 15 × 3", "15 reps × 3 sets • Drive through heels, squeeze glutes at top"),
                RoutineItem("w_fri_4", "Calf Raises • 20 × 3", "20 reps × 3 sets • Full stretch at bottom, pause at top")
            )
            Calendar.SATURDAY -> "SAT: SHOULDERS, CHEST & ABS" to listOf(
                RoutineItem("w_sat_1", "Pike Push-ups • 8 × 3", "8 reps × 3 sets • Vertical shoulder press angle"),
                RoutineItem("w_sat_2", "Standard Push-ups • 10 × 3", "10 reps × 3 sets • Full range, rigid core"),
                RoutineItem("w_sat_3", "Decline Push-ups • 8 × 2", "8 reps × 2 sets • Feet elevated, upper chest & front delts"),
                RoutineItem("w_sat_4", "Lying Leg Raises • 10 × 3", "10 reps × 3 sets • Straight/controlled legs, low back down"),
                RoutineItem("w_sat_5", "Hollow Body Hold • 20 sec × 3", "20 sec × 3 sets • Hold tension without arching back"),
                RoutineItem("w_sat_6", "Side Plank • 30 sec per side × 2", "30 sec per side × 2 sets • Hips lifted, straight alignment")
            )
            else -> "SUN: COMPLETE FREE" to listOf(
                RoutineItem("w_sun_1", "Full Recovery & Free Day • 1 day", "Full recovery and Free day")
            )
        }
    }

    fun getVocalRegimen(): List<RoutineItem> = listOf(
        RoutineItem("v_1", "Posture Check • 5 min × 1", "Feet shoulder-width, spine neutral, shoulders relaxed"),
        RoutineItem("v_2", "Lip Trills • 2 min × 1", "Exhale steadily through loose lips, slide pitch low to high & down"),
        RoutineItem("v_3", "Tongue Trills • 2 min × 1", "Rolled 'R' pitch slides to isolate tongue"),
        RoutineItem("v_4", "The Hiss • 20 to 30 sec × 4", "Inhale deeply (ribcage expands), exhale slowly on long 'S' sound"),
        RoutineItem("v_5", "Pulsed Staccato • 10 pulses × 3", "Short 'Ha, Ha, Ha' bursts on single note using lower abs"),
        RoutineItem("v_6", "Humming on M • 5 min × 1", "Teeth apart behind closed lips; feel vibration in nose bridge & forehead"),
        RoutineItem("v_7", "Vowel Shaping • 5 reps × 1", "Start 'Ng' sound, smoothly open to 'Ah' across range without changing resonance"),
        RoutineItem("v_8", "Interval Scales • 5 min × 1", "Play starting note, sing major scale with solfege/vowels, hit intervals cleanly (no sliding)")
    )

    fun getAccentDrills(): List<RoutineItem> = listOf(
        RoutineItem("a_1", "V vs. W Lip-Teeth Split • 5 min", "Top teeth lightly on bottom lip for V (vine); round lips with no teeth touch for W (wine)"),
        RoutineItem("a_2", "Alveolar T/D Placement • 5 min", "Tongue tip flat on gum ridge behind upper teeth (not curled back); crisp puff on stressed T"),
        RoutineItem("a_3", "Continuous TH Airflow • 5 min", "Tongue tip lightly between front teeth with steady air (think = unvoiced, this = voiced)"),
        RoutineItem("a_4", "Diphthongs & Vowel Contrast • 5 min", "Glide vowels smoothly in 'day' and 'go'; separate short vs. long pairs (ship/sheep, full/fool)"),
        RoutineItem("a_5", "Word Stress & Clusters • 5 min", "Stress one main syllable per word; keep clusters tight (school, film) without extra vowel sounds"),
        RoutineItem("a_6", "Record & Playback Check • 5 min", "Read 2-3 sentences aloud, record on phone, and listen back to spot slips")
    )
}
