package repo

trait ScalaToLanguageBridge[ScalaValue] {
  def apply[Wrapper[_]](implicit L: Language[Wrapper]): Wrapper[ScalaValue]
}
