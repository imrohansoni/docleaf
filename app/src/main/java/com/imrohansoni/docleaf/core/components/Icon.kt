@file:Suppress("unused")

package com.imrohansoni.docleaf.core.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import com.imrohansoni.docleaf.R
import com.imrohansoni.docleaf.core.theme.LocalDimensions

class AppIcon(@param:DrawableRes val resId: Int)



object Icons {
    val Alert = AppIcon(R.drawable.alert)
    val AlignSelection = AppIcon(R.drawable.align_selection)
    val Arrow = AppIcon(R.drawable.arrow)
    val ArrowAllDirection = AppIcon(R.drawable.arrow_all_direction)
    val ArrowDown = AppIcon(R.drawable.arrow_down)
    val ArrowLeft = AppIcon(R.drawable.arrow_left)
    val ArrowRight = AppIcon(R.drawable.arrow_right)
    val Book = AppIcon(R.drawable.book)
    val Box = AppIcon(R.drawable.box)
    val BoxFill = AppIcon(R.drawable.box_fill)
    val Controller = AppIcon(R.drawable.controller)
    val CropPlus = AppIcon(R.drawable.copy_plus)
    val Crop = AppIcon(R.drawable.crop)
    val Delete = AppIcon(R.drawable.delete)
    val Download = AppIcon(R.drawable.download)
    val EditText = AppIcon(R.drawable.edit_text)
    val File = AppIcon(R.drawable.file)
    val Flip = AppIcon(R.drawable.flip)
    val Folder = AppIcon(R.drawable.folder)
    val FlashlightOn = AppIcon(R.drawable.flash)
    val FlashlightOff = AppIcon(R.drawable.flash_off)
    val FolderFill = AppIcon(R.drawable.folder_fill)
    val Grid = AppIcon(R.drawable.grid)
    val Highlighter = AppIcon(R.drawable.highlighter)
    val Home = AppIcon(R.drawable.home)
    val HomeFill = AppIcon(R.drawable.home_fill)
    val IdCard = AppIcon(R.drawable.id_card)
    val Image = AppIcon(R.drawable.image)
    val Lock = AppIcon(R.drawable.lock)
    val MagicWand = AppIcon(R.drawable.magic_wand)
    val Mail = AppIcon(R.drawable.mail)
    val Pen = AppIcon(R.drawable.pen)
    val QrCode = AppIcon(R.drawable.qr_code)
    val Redo = AppIcon(R.drawable.redo)
    val Rotate = AppIcon(R.drawable.rotate)
    val Scan = AppIcon(R.drawable.scan)
    val Search = AppIcon(R.drawable.search)
    val SecurityWarning = AppIcon(R.drawable.security_warning)
    val Settings = AppIcon(R.drawable.settings)
    val Signature = AppIcon(R.drawable.signature)
    val TextSquare = AppIcon(R.drawable.text_square)
    val Undo = AppIcon(R.drawable.undo)
    val User = AppIcon(R.drawable.user)
    val UserCircle = AppIcon(R.drawable.user_circle)
    val UserCircleFill = AppIcon(R.drawable.user_circle_fill)
    val UserSquare = AppIcon(R.drawable.user_square)
}

@Composable
fun Icon(
    modifier: Modifier = Modifier,
    icon: AppIcon,
    size: Dp = LocalDimensions.current.iconMedium,
    color: Color? = null,
) {
    Image(
        modifier = modifier.size(size),
        painter = painterResource(icon.resId),
        contentDescription = "",
        colorFilter = color?.let { ColorFilter.tint(it) }
    )
}


