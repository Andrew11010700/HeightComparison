package ua.scootersoft.heightcomparison.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

@Composable
fun Toolbar(
    text: String,
    leftImgRes: Int? = null,
    rightImgRes: Int? = null,
    onLeftImageAction: () -> Unit = {},
    onRightImageAction: () -> Unit = {}
) {
    ConstraintLayout(modifier = Modifier.fillMaxWidth()) {

        val (leftImg, centerText, rightImg) = createRefs()

        if (leftImgRes != null) {
            Image(
                painter = painterResource(id = leftImgRes),
                contentDescription = "add Person",
                modifier = Modifier
                    .constrainAs(leftImg) {
                        top.linkTo(centerText.top)
                        bottom.linkTo(centerText.bottom)
                        start.linkTo(parent.start, 16.dp)
                    }
                    .clip(CircleShape)
                    .clickable {
                        onLeftImageAction()
                    }
                    .padding(8.dp)
            )
        }

        Text(
            text = text,
            modifier = Modifier
                .constrainAs(centerText) {
                    top.linkTo(parent.top, 16.dp)
                    bottom.linkTo(parent.bottom, 16.dp)
                    start.linkTo(if (leftImgRes != null) leftImg.end else parent.start, 16.dp)
                    end.linkTo( if (rightImgRes != null) rightImg.start else parent.end, 16.dp)
                    width = Dimension.fillToConstraints
                },
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )

        if (rightImgRes != null) {
            Image(
                painter = painterResource(id = rightImgRes),
                contentDescription = "edit Person",
                modifier = Modifier
                    .constrainAs(rightImg) {
                        end.linkTo(parent.end, 16.dp)
                        top.linkTo(centerText.top)
                        bottom.linkTo(centerText.bottom)
                    }
                    .clip(CircleShape)
                    .clickable {
                        onRightImageAction()
                    }
                    .padding(8.dp)
            )
        }

    }
}