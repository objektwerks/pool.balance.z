package objektwerks.dialog

import objektwerks.{Cleaning, Context}

final class CleaningDialog(cleaning: Cleaning) extends Dialog(Context.cleaning):
  var editedCleaning = cleaning.copy()